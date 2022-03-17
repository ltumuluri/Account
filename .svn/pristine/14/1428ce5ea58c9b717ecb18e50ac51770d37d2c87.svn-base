package org.uftwf.account.util;

import org.uft.plunkit.ConnectionFactory;
import org.uftwf.account.model.*;
import org.uftwf.ssoclient.SSOClient;
import org.uftwf.ssoclient.model.KeycloakUser;

import java.util.*;

/**
 * Created by xyang on 4/28/17.
 */
public class UserHelper {

    public StatusMessage needToUpdate(KeycloakUser existingUser, KeycloakUser newUserInfo, SSOClient client){
        boolean update=false;
        String message="";
        StatusMessage statusMessage = new StatusMessage();
        if(!existingUser.getLastname().equalsIgnoreCase(newUserInfo.getLastname())
                ||!existingUser.getFirstname().equalsIgnoreCase(newUserInfo.getFirstname())||
                !existingUser.getEmail().equalsIgnoreCase(newUserInfo.getEmail())||
                !existingUser.getUsername().equalsIgnoreCase(newUserInfo.getUsername())
                ){

            update=true;
            if(!existingUser.getEmail().equalsIgnoreCase(newUserInfo.getEmail())){
                if(client.emailExists(newUserInfo.getEmail())){
                    message=message+newUserInfo.getEmail()+" Already Existed#";
                    update=false;
                }
            }
            if(!existingUser.getUsername().equalsIgnoreCase(newUserInfo.getUsername())){
                if(client.usernameExists(newUserInfo.getUsername())){
                    message=message+newUserInfo.getUsername()+" Already Existed#";
                    update=false;
                }
            }
        }
        if(existingUser.getUserAttributes()==null&&newUserInfo.getUserAttributes()!=null){
            update=true;
        }
        if(existingUser.getUserAttributes()!=null&&existingUser.getUserAttributes().get("zipCode")!=null&&newUserInfo.getUserAttributes()==null){
            update=true;
        }
        else if(existingUser.getUserAttributes()!=null&&existingUser.getUserAttributes().get("zipCode")!=null&&newUserInfo.getUserAttributes()!=null){
            String zipCode=existingUser.getUserAttributes().get("zipCode").get(0);
            String newZipCode=newUserInfo.getUserAttributes().get("zipCode").get(0);
            if (!zipCode.equalsIgnoreCase(newZipCode)) {

                update=true;
            }
        }
        statusMessage.setMessage(message);
        statusMessage.setStatus(update);
        return statusMessage;
    }
    public KeycloakUser convertToKeyCloakUser(PersonalInfo personalInfo){
        KeycloakUser result= new KeycloakUser();
        result.setFirstname(personalInfo.getFirstname());
        result.setUsername(personalInfo.getUsername());
        result.setLastname(personalInfo.getLastname());
        result.setEmail(personalInfo.getEmail());
        if(personalInfo.getZipCode().length()>0){
            Map<String, List<String>> userAttributes= new HashMap<String, List<String>>();
            List zipCodeList= new ArrayList();
            zipCodeList.add(personalInfo.getZipCode());
            userAttributes.put("zipCode",zipCodeList);
            result.setUserAttributes(userAttributes);
        }
        return result;
    }
    public boolean As400Connection(){
        ConnectionFactory factory= new ConnectionFactory();
        return factory.canConnect();
    }
    public SuggestMemberApp[] ConvertSetToArray(Set<SuggestMemberApp> appSet){
        int setLength=appSet.size();
        SuggestMemberApp[] resultArray = new SuggestMemberApp[setLength];
        int index=0;
        for(SuggestMemberApp app : appSet){
            resultArray[index]=app;
            index++;
        }
        Arrays.sort(resultArray, new Comparator<SuggestMemberApp>() {
            @Override
            public int compare(SuggestMemberApp suggestMemberApp, SuggestMemberApp t1) {

                return suggestMemberApp.getPriority()-t1.getPriority();
            }
        });
        return resultArray;
    }
    public NonMemberApplication[] ConvertNonMemberToArray(Set<NonMemberApplication> appSet){
        int setLength=appSet.size();
        NonMemberApplication[] resultArray = new NonMemberApplication[setLength];
        int index=0;
        for(NonMemberApplication app : appSet){
            resultArray[index]=app;
            index++;
        }
        Arrays.sort(resultArray, new Comparator<NonMemberApplication>() {
            @Override
            public int compare(NonMemberApplication suggestMemberApp, NonMemberApplication t1) {

                return suggestMemberApp.getPriority()-t1.getPriority();
            }
        });
        return resultArray;
    }
}
