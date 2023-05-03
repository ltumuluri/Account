package org.uftwf.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.uft.plunkit.*;
import org.uftwf.account.model.*;
import org.uftwf.account.service.MySqlService;
import org.uftwf.account.service.SalesforceService;
import org.uftwf.account.service.UserService;
import org.uftwf.account.util.MySqlConnectionFactory;
import org.uftwf.account.util.PropertiesHelper;
import org.uftwf.account.util.UserHelper;
import org.uftwf.ssoclient.KeycloakHttp;
import org.uftwf.ssoclient.SSOClient;
import org.uftwf.ssoclient.model.KeycloakUser;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by xyang on 4/27/17.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
    @Autowired
    UserService userService;
    @Autowired
    SalesforceService salesforceService;
    SSOClient client = new SSOClient("uft",true);
    KeycloakHttp keycloakHttp = new KeycloakHttp();

    @RequestMapping(value = "/versionInfo", method = RequestMethod.GET)
    public ProjectVersion getProjectVersion() {
        LOGGER.info("getProjectVersion: return project information" + "\r\n");
        PropertiesHelper helper = new PropertiesHelper();
        Properties properties = helper.getProperties("/application.properties");
        ProjectVersion version = new ProjectVersion();
        if (properties != null) {
            version.setName(properties.getProperty("name"));
            version.setDescription(properties.getProperty("description"));
            version.setVersion(properties.getProperty("version"));
        }
        return version;
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ObjectDB getUserModel() {
        LOGGER.info("getUserModel: return user information" + "\r\n");
        String userId = userService.getUserId();
        KeycloakUser user = client.getAllUserInfo(userId);
        String memberId=userService.getMemberId();

        UserInfo userInfo = new UserInfo();
        boolean dbStatus=true;
        boolean isCCP= userService.isCCP();
        userInfo.setCCP(isCCP);
        if(memberId!=null&&Integer.parseInt(memberId)>0){
            CallResponse callResponse=salesforceService.getEmailOptOutStatus(memberId);
            if(callResponse.isStatus()){
                boolean flag = (boolean)callResponse.getMessage();
                userInfo.setEmailOptOut(flag);
            }
            userInfo.setMember(true);
            ObjectDB objectDB = MySqlService.getInstance().optInNumber(memberId);
            if(objectDB.isDbStatus()) {
                ObjectDB uftId = MySqlService.getInstance().uftId(memberId);
                userInfo.setMemberId((String) uftId.getDbObject());
                if (objectDB.getDbObject() != null) {
                    userInfo.setOptin(true);
                    userInfo.setOptInNumber((String) objectDB.getDbObject());
                }
            }
            else{
                dbStatus=false;
            }
        }
        else{
            userInfo.setMember(false);
        }
        userInfo.setUser(user);
        userService.setSelectedUser(userInfo);
        userService.setInitialEmail(user.getEmail());
        ObjectDB returnObject = new ObjectDB();
        returnObject.setDbStatus(dbStatus);
        returnObject.setDbObject(userInfo);
        return returnObject;
    }
    @RequestMapping(value="/old_email", method=RequestMethod.GET)
    public void oldEmail(HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.write(userService.getInitialEmail());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/forbidden_domain", method = RequestMethod.GET)
    public ArrayList<String> getForbiddenDomain() {
        LOGGER.info("getForbiddenDomain: return all the forbidden email domain" + "\r\n");
        return MySqlService.getInstance().getForbiddenDomain();
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@Valid @RequestBody UserUpdate user, HttpServletRequest request, HttpServletResponse response, BindingResult result) {
        LOGGER.info("updateUser: validate user information and return the status to front end" + "\r\n");
        UserUpdateValidator validator = new UserUpdateValidator();
        validator.validate(user, result);
        try {
            if (result.hasErrors()) {

                System.out.println(result.getAllErrors());
                String errorMessage = "";
                List<FieldError> errorList = result.getFieldErrors();

                for (FieldError error : errorList) {
                    errorMessage += error.getDefaultMessage() + "#";

                }
                PrintWriter out = response.getWriter();
                out.write(errorMessage);
                out.close();
            } else {
                UserInfo existUserInfo = userService.getSelectedUser();
                String errorMessage="";
                boolean update=true;
                UserHelper helper = new UserHelper();
                KeycloakUser convertPersonalInfoToUserInfo = helper.convertToKeyCloakUser(user.getUser());
                KeycloakUser exsitingKeycloakUser = existUserInfo.getUser();
                StatusMessage statusMessage = helper.needToUpdate(exsitingKeycloakUser,convertPersonalInfoToUserInfo,client);
                update = statusMessage.isStatus();
               if(statusMessage.getMessage()!=null&&statusMessage.getMessage().length()>0){
                   errorMessage=errorMessage+statusMessage.getMessage();
               }
                if(user.getNewpassword()!=null&&user.getConfirmpassword()!=null){
                    if(user.getNewpassword().equalsIgnoreCase(user.getConfirmpassword())){
                            String userId = userService.getUserId();
                            client.updateUserPassword(userId, user.getNewpassword());
                    }
                    else{
                        errorMessage=errorMessage+"Password doesn't match with Confirm Password#";
                        update=false;
                    }
                }
                if(user.isEmailOptOut()!=null){
                   CallResponse callResponse= salesforceService.updateEmailOptOut(userService.getMemberId(),user.isEmailOptOut());
                   if(!callResponse.isStatus()){
                       PrintWriter out = response.getWriter();
                       out.write("Email is not subscribe");
                       out.close();
                   }
                }
                boolean optin = false;
                if(user.getOptin().equalsIgnoreCase("yes")){
                    optin=true;
                }

                if(MySqlConnectionFactory.canConnect()) {
                    if(existUserInfo.getOptInNumber()==null){
                        existUserInfo.setOptInNumber("");
                    }
                    if (existUserInfo.isOptin() != optin||
                            !existUserInfo.getOptInNumber().equals(user.getPhoneField())) {
                        String phone = user.getPhoneField();
                        if (optin) {
                            //book.updateEmailAddress(user.getMemberId(),user.getUser().getEmail(),phone);
                            MySqlService.getInstance().insertPhoneNumberIntoTemp(userService.getMemberId(), phone, "yes");
                            MySqlService.getInstance().insertOptinIntoMemberExt(phone, userService.getMemberId());
                        } else {
                            MySqlService.getInstance().insertPhoneNumberIntoTemp(userService.getMemberId(), "", "no");
                            MySqlService.getInstance().insertOptinIntoMemberExt(null, userService.getMemberId());
                        }
                    }
                    if (update) {
                        String userId = userService.getUserId();
                        exsitingKeycloakUser.setFirstname(convertPersonalInfoToUserInfo.getFirstname());
                        exsitingKeycloakUser.setLastname(convertPersonalInfoToUserInfo.getLastname());
                        ArrayList<String> forbiddenList = MySqlService.getInstance().getForbiddenDomain();
                        boolean containForbiddenDomain=false;
                        String errorDomain="";
                        for(int i=0;i<forbiddenList.size();i++){
                            if((convertPersonalInfoToUserInfo.getEmail().toUpperCase()).indexOf(forbiddenList.get(i).toUpperCase())>0){
                                containForbiddenDomain=true;
                                errorDomain=forbiddenList.get(i);
                                break;
                            }
                        }
                        if(containForbiddenDomain){
                                errorMessage=errorMessage+"Please provide an email address that is not from the "+errorDomain +" domain#";
                                errorMessage = errorMessage.substring(0, errorMessage.lastIndexOf("#"));
                                PrintWriter out = response.getWriter();
                                out.write(errorMessage);
                                out.close();
                        }
                        else {
                            if (!exsitingKeycloakUser.getEmail().equalsIgnoreCase(convertPersonalInfoToUserInfo.getEmail()) && !containForbiddenDomain) {
                                exsitingKeycloakUser.setEmail(convertPersonalInfoToUserInfo.getEmail());
                                if (existUserInfo.getMemberId() != null && existUserInfo.getMemberId().length() > 0) {
                                    MySqlService.getInstance().insertEmailIntoTempTable(convertPersonalInfoToUserInfo.getEmail(), userService.getMemberId());
                                }
                                try {
                                    InitialContext ctx = new InitialContext();
                                    String url = (String) ctx.lookup("java:global/uft/account");
                                    client.triggerEmailVerification(userId, url, convertPersonalInfoToUserInfo.getEmail());
                                    userService.setEmailUpdated(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            exsitingKeycloakUser.setUsername(convertPersonalInfoToUserInfo.getUsername());
                            Map<String, List<String>> userAttributes = exsitingKeycloakUser.getUserAttributes();
                            Map<String, List<String>> userAttributeNew = convertPersonalInfoToUserInfo.getUserAttributes();
                            if (userAttributes != null && userAttributeNew != null) {
                                List<String> zipCodeList = userAttributeNew.get("zipCode");
                                userAttributes.put("zipCode", zipCodeList);
                                exsitingKeycloakUser.setUserAttributes(userAttributes);
                            }

                            client.updateUserInfo(userId, exsitingKeycloakUser);

                            PrintWriter out = response.getWriter();
                            out.write("success");
                            out.close();
                        }
                    } else {
                        if (errorMessage.length() > 0) {
                            errorMessage = errorMessage.substring(0, errorMessage.lastIndexOf("#"));
                            PrintWriter out = response.getWriter();
                            out.write(errorMessage);
                            out.close();
                        } else {

                            PrintWriter out = response.getWriter();
                            out.write("success");
                            out.close();
                        }
                    }
                }else{
                    PrintWriter out = response.getWriter();
                    out.write("dbIssue");
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value ="/verifylink")
    public String getVerifyLink(){
        InitialContext ctx = null;

        try {
            ctx = new InitialContext();
            String url = (String) ctx.lookup("java:global/uft/verify");
            return url;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;

    }
    @RequestMapping(value="/getNonMemberApp")
    public NonMemberApplication[] getNonMemberApps(){
        if((userService.getMemberId()==null||Integer.parseInt(userService.getMemberId())<0)&&!userService.isCCP()) {
            NonMemberApplication[] resultArray=null;
            Set<NonMemberApplication> appLists = new HashSet<NonMemberApplication>();
            appLists.add(NonMemberApps.getSuggestApp(NonMemberApps.NonMemberApp.UNION));
            appLists.add(NonMemberApps.getSuggestApp(NonMemberApps.NonMemberApp.WELFARE));
            if(appLists.size()>0){
                UserHelper helper = new UserHelper();
                resultArray = helper.ConvertNonMemberToArray(appLists);
            }
            return resultArray;
        }else{
            return null;
        }
    }
    @RequestMapping(value="/getAccessApp")
    public NonMemberApplication[] geAccessApps(){
        if((userService.getMemberId()==null||Integer.parseInt(userService.getMemberId())<0)&&!userService.isCCP()) {
            NonMemberApplication[] resultArray=null;
            Set<NonMemberApplication> appLists = new HashSet<NonMemberApplication>();
            appLists.add(NonMemberApps.getSuggestApp(NonMemberApps.NonMemberApp.VERIFY));
            appLists.add(NonMemberApps.getSuggestApp(NonMemberApps.NonMemberApp.CCP));
            if(appLists.size()>0){
                UserHelper helper = new UserHelper();
                resultArray = helper.ConvertNonMemberToArray(appLists);
            }
            return resultArray;
        }else{
            return null;
        }
    }
//    @RequestMapping(value="/hasWebSection")
//    public SuggestMemberApp[] hasWebSection(){
//        String ssoId=userService.getUserId();
//        String[] chapterSection={"ICW_group","AM_group","OCC_group","CCW_group","FC_group","DR_group","CC_group","DR_group","BR_group","SRW_group","CADV_group","UDS_group","LAC_group"};
//        String[] visitingNurseSection={"LH_group","JHHA_group","SI_group","VN_group","GUIL_group"};
//        String[] peerSection={"PIPW_group"};
//        Set<SuggestMemberApp> appLists = new HashSet<SuggestMemberApp>();
//        SuggestMemberApp[] resultArray= null;
//        for(String group: chapterSection){
//            if(client.hasUserGroup(ssoId,group)){
//                appLists.add(Apps.getSuggestApp(Apps.App.CHAPTERSECTION));
//                break;
//            }
//        }
//        for(String group: visitingNurseSection){
//            if(client.hasUserGroup(ssoId,group)){
//                appLists.add(Apps.getSuggestApp(Apps.App.NURSESECTION));
//                break;
//            }
//        }
//        for(String group:peerSection){
//            if(client.hasUserGroup(ssoId,group)){
//                appLists.add(Apps.getSuggestApp(Apps.App.PEERSECTION));
//            }
//        }
//        if(client.hasUserGroup(ssoId,"member")){
//            appLists.add(Apps.getSuggestApp(Apps.App.DISCOUTSECTION));
//            appLists.add(Apps.getSuggestApp(Apps.App.JUSTFORFUNSECTION));
//            appLists.add(Apps.getSuggestApp(Apps.App.MOVIETICKET));
//        }
//        if(appLists.size()>0){
//            UserHelper helper = new UserHelper();
//            resultArray = helper.ConvertSetToArray(appLists);
//        }
//        return resultArray;
//    }
    @RequestMapping(value="/suggestApp")
    public SuggestMemberApp[] getSuggestApp(){
        String memberId = userService.getMemberId();
        SuggestMemberApp[] resultArray=null;

        Set<SuggestMemberApp> appLists = new HashSet<SuggestMemberApp>();
        List<UnionEnrollmentData> enrollmentDatas =new ArrayList<UnionEnrollmentData>();
        List<WelfareData> welfareDatas = new ArrayList<WelfareData>();
        appLists.add(Apps.getSuggestApp(Apps.App.COURSES));
        appLists.add(Apps.getSuggestApp(Apps.App.FORMSDOCUMENTS));
        if(!userService.isCCP()) {
            try {

                InitialContext ctx = new InitialContext();
                String host = (String) ctx.lookup("java:global/verify/url");

                if (memberId == null) {
                    appLists.add(Apps.getSuggestApp(Apps.App.UNION));
                    appLists.add(Apps.getSuggestApp(Apps.App.WELFARE));
                    // appLists.add(Apps.getSuggestApp(Apps.App.VERIFY));
                } else {
                    MemberExtData memberData = MySqlService.getInstance().getMemberExtData(memberId);

                    if (memberData != null) {
                        appLists.add(Apps.getSuggestApp(Apps.App.UNION));
                        userService.setMemberData(memberData);
                        if (memberData.getUnion_eligible() != null && memberData.getUnion_eligible().equalsIgnoreCase("Y") && memberData.getEnrollment_date() != null && memberData.getEnrollment_date().length() > 0) {
                            appLists.add(Apps.getSuggestApp(Apps.App.UNIONQUIRY));
                            appLists.add(Apps.getSuggestApp(Apps.App.UNIONCOS));
                        } else if (memberData.getEnrollment_date() == null && memberData.getUnion_eligible() != null && memberData.getUnion_eligible().equalsIgnoreCase("Y")) {
                            RecordStatus enrollmentStatus = MySqlService.getInstance().existingUnion(memberId);
                            if (enrollmentStatus.isDbStatus()) {
                                if (enrollmentStatus.getRecordStatus() != null && !enrollmentStatus.getRecordStatus().equalsIgnoreCase("M")) {
                                    appLists.add(Apps.getSuggestApp(Apps.App.UNION));
                                } else if (enrollmentStatus.getRecordStatus() == null) {
                                    appLists.add(Apps.getSuggestApp(Apps.App.UNION));
                                } else if (enrollmentStatus.getRecordStatus() != null && enrollmentStatus.getRecordStatus().equalsIgnoreCase("M")) {
                                    appLists.add(Apps.getSuggestApp(Apps.App.UNIONQUIRY));
                                    appLists.add(Apps.getSuggestApp(Apps.App.UNIONCOS));
                                }

                            }
                        }
                        if (memberData.getWfCovered() != null && memberData.getWfCovered().equalsIgnoreCase("Y") && memberData.getWf_date() != null && memberData.getWf_date().length() > 0) {
                            appLists.add(Apps.getSuggestApp(Apps.App.COS));
                            if (appLists.contains(Apps.getSuggestApp(Apps.App.UNIONCOS))) {
                                appLists.remove(Apps.getSuggestApp(Apps.App.UNIONCOS));
                            }
                            appLists.add(Apps.getSuggestApp(Apps.App.WELFAREINQUIRY));
                            SuggestMemberApp certificateApp = Apps.getSuggestApp(Apps.App.CERTIFICATE);
                            SuggestMemberApp opticalApp = Apps.getSuggestApp(Apps.App.OPTICAL);
                            if (memberData.getCertificate_eligible() != null && memberData.getCertificate_eligible().equalsIgnoreCase("N")) {
                                certificateApp.setStatus(false);
                                certificateApp.setDescription("Currently, there are no available certificates .");
                            }
                            appLists.add(certificateApp);
                            appLists.add(opticalApp);
                        } else if (memberData.getWf_date() == null && memberData.getWfCovered().equalsIgnoreCase("Y")) {
                            RecordStatus welfareStatus = MySqlService.getInstance().existingWelfare(memberId);
                            if (welfareStatus.isDbStatus()) {
                                if (welfareStatus.getRecordStatus()==null||(welfareStatus.getRecordStatus()!=null&&!welfareStatus.getRecordStatus().equalsIgnoreCase("ENROLLED"))) {
                                    appLists.add(Apps.getSuggestApp(Apps.App.WELFARE));
                                }
                            }
                        }
                    }
                    String ssoId = userService.getUserId();
//                    if (client.hasUserGroup(ssoId, "ADM_group")) {
//                        appLists.add(Apps.getSuggestApp(Apps.App.UNIONQUEUE));
//                    }
                    if(client.hasUserGroup(ssoId,"OCC_group")||client.hasUserGroup(ssoId,"BR_group")){
                        appLists.add(Apps.getSuggestApp(Apps.App.NONMEMBERREPORT));
                    }
//                    if(client.hasUserGroup(ssoId,"GCC_group")){
//                        String appUrl =System.getProperty("memberCommunity");
//                        SuggestMemberApp salesforceapp =Apps.getSuggestApp(Apps.App.SALESFORCECONSELORCOMMUNITY);
//                        salesforceapp.setAppUrl(appUrl);
//                        appLists.add(salesforceapp);
//                    }

                    //need to removed


                }
                if (appLists.size() > 0) {
                    UserHelper helper = new UserHelper();
                    resultArray = helper.ConvertSetToArray(appLists);
                }
                return resultArray;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            return null;
        }
        return null;
    }
    @RequestMapping(value="/hasChpaterLeaderSection",method=RequestMethod.GET)
    public SuggestMemberApp[] hasChpaterLeaderSection(){
        String memberId = userService.getMemberId();
        SuggestMemberApp[] resultArray=null;
        Set<SuggestMemberApp> appLists = new HashSet<SuggestMemberApp>();
        String ssoId = userService.getUserId();
        if (client.hasUserGroup(ssoId, "CC_group")||client.hasUserGroup(ssoId,"CCW_group")||client.hasUserGroup(ssoId,"RDR_group")||client.hasUserGroup(ssoId,"FC_group")) {
            appLists.add(Apps.getSuggestApp(Apps.App.PAPERFORMREDUCTION));
            appLists.add(Apps.getSuggestApp(Apps.App.CONSULATIONCOMMITTEE));
            if(client.hasUserGroup(ssoId,"CC_group")||client.hasUserGroup(ssoId,"RDR_group")) {
                appLists.add(Apps.getSuggestApp(Apps.App.GRIEVANCE));
                appLists.add(Apps.getSuggestApp(Apps.App.GRIEVANCEQUEUE));
                appLists.add(Apps.getSuggestApp(Apps.App.NONMEMBERREPORT));
            }
        }
        if (appLists.size() > 0) {
            UserHelper helper = new UserHelper();
            resultArray = helper.ConvertSetToArray(appLists);
        }
        return resultArray;

    }

    @RequestMapping(value="/hasRetireeSection",method=RequestMethod.GET)
    public SuggestMemberApp[] hasRetireeSection(){
        String memberId = userService.getMemberId();
        SuggestMemberApp[] resultArray=null;
        Set<SuggestMemberApp> appLists = new HashSet<SuggestMemberApp>();
        String ssoId = userService.getUserId();
        if (client.hasUserGroup(ssoId, "RETIREE_group")) {
            //for resultArray != null
            //applists will not show in any case
            appLists.add(Apps.getSuggestApp(Apps.App.GRIEVANCE));
        }
        if (appLists.size() > 0) {
            UserHelper helper = new UserHelper();
            resultArray = helper.ConvertSetToArray(appLists);
        }
        return resultArray;
    }


    @RequestMapping(value="/rest/version",method = RequestMethod.GET)
    public ProjectVersion restVersion(){
        PropertiesHelper helper = new PropertiesHelper();
        Properties properties = helper.getProperties("/application.properties");
        ProjectVersion version = new ProjectVersion();
        if (properties != null) {
            version.setName(properties.getProperty("name"));
            version.setDescription(properties.getProperty("description"));
            version.setVersion(properties.getProperty("version"));
        }
        return version;
    }
    @RequestMapping(value="/hasStaffSection",method=RequestMethod.GET)
    public SuggestMemberApp[] hasStaffSection(){
        String memberId = userService.getMemberId();
        SuggestMemberApp[] resultArray=null;
        Set<SuggestMemberApp> appLists = new HashSet<SuggestMemberApp>();
        String ssoId = userService.getUserId();
        if (client.hasUserGroup(ssoId, "WFQA_group")||client.hasUserGroup(ssoId,"WFQU_group")){
            appLists.add(Apps.getSuggestApp(Apps.App.WELFAREQUEUE));
        }
        if (client.hasUserGroup(ssoId, "COSA_group")||client.hasUserGroup(ssoId,"COSU_group")){
            appLists.add(Apps.getSuggestApp(Apps.App.COSQUEUE));
        }

        if (appLists.size() > 0) {
            UserHelper helper = new UserHelper();
            resultArray = helper.ConvertSetToArray(appLists);
        }
        return resultArray;

    }
    @RequestMapping(value="/hasHelpDeskSection",method = RequestMethod.GET)
    public SuggestMemberApp[] hasHelpDeskSection(){
        String memberId = userService.getMemberId();
        SuggestMemberApp[] resultArray=null;
        Set<SuggestMemberApp> appLists = new HashSet<SuggestMemberApp>();
        String ssoId = userService.getUserId();
        if (client.hasUserGroup(ssoId, "MHD_group")) {
           appLists.add(Apps.getSuggestApp(Apps.App.HELPDESKCONSOLE));
        }
        if (client.hasUserGroup(ssoId, "ADM_group")) {
            appLists.add(Apps.getSuggestApp(Apps.App.UNIONQUEUE));
        }

        if (appLists.size() > 0) {
            UserHelper helper = new UserHelper();
            resultArray = helper.ConvertSetToArray(appLists);
        }
        return resultArray;
    }
    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(HttpEntity<String> httpEntity, HttpServletResponse response) {
          String memberId = httpEntity.getBody();
        if (memberId != null && memberId.length() > 0) {
            userService.setMemberId(memberId);
            if (MySqlService.getInstance().existingMember(memberId)) {
                MySqlService.getInstance().updateEmailStatus(memberId);
            }
        }
    }
    @RequestMapping(value="/getEmailUpdateStatus")
    public boolean emailUpdate(){
        return userService.isEmailUpdated();
    }
    @RequestMapping(value="/mysqlStatus",method=RequestMethod.GET)
    public void mysqlStatus(HttpServletResponse response){
        if(MySqlConnectionFactory.canConnect()){
            response.setStatus(200);

        }
        else{
            try {
                response.sendError(500, "mysql-wfservices not working");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @RequestMapping(value="/as400Status",method=RequestMethod.GET)
    public void as400Status(HttpServletResponse response){
        if(ConnectionFactory.canConnect()){
            response.setStatus(200);
           ArrayList<String> domain= MySqlService.getInstance().getForbiddenDomain();
            ObjectMapper mapper =new ObjectMapper();
            try {
                PrintWriter out = response.getWriter();
                out.write(mapper.writeValueAsString(domain));
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else{
            try {
                response.sendError(500, "as400-wfservices not working");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @RequestMapping(value="/showCommunityBanner",method=RequestMethod.GET)
    public void showBanner(HttpServletRequest request, HttpServletResponse response){
        String member_id = userService.getMemberId();
        if(member_id!=null){
             String experience= System.getProperty("communityExperience");
             if(experience==null||experience.equalsIgnoreCase("false")){
                 try {
                     PrintWriter out = response.getWriter();
                     out.write("none");
                     out.close();
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
             else{
                 boolean allowAccess =MySqlService.getInstance().getCommunityFlag(member_id);
                 if(allowAccess){
                     try {
                         PrintWriter out = response.getWriter();
                         String link= System.getProperty("communityExperienceLink");
                         out.write(link);
                         out.close();
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
                 else{
                     try {
                         PrintWriter out = response.getWriter();
                         out.write("none");
                         out.close();
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
             }

        }
        else{
            try {
                PrintWriter out = response.getWriter();
                out.write("none");
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
