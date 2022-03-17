package org.uftwf.account.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.uft.plunkit.ConnectionFactory;
import org.uftwf.account.model.MemberExtData;
import org.uftwf.account.model.UnionEnrollmentData;
import org.uftwf.account.model.UserInfo;
import org.uftwf.account.model.WelfareData;

/**
 * Created by xyang on 4/27/17.
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {
    private  String userId;
    private  UserService userService;
    private UserInfo selectedUser;
    private String memberId;
    private UnionEnrollmentData unionEnrollmentData;
    private WelfareData welfareData;
    private MemberExtData memberData;
    private boolean emailUpdated=false;
    private String initialEmail;
    private boolean isCCP;
    public void cleanUserService(){
        this.userId=null;
        this.userService=null;
        this.selectedUser=null;
        this.memberId=null;
        this.unionEnrollmentData=null;
        this.welfareData=null;
        this.memberData=null;
        this.emailUpdated=false;
        this.initialEmail=null;
        this.isCCP=false;
    }

    public boolean isCCP() {
        return isCCP;
    }

    public void setCCP(boolean CCP) {
        isCCP = CCP;
    }

    public String getInitialEmail() {
        return initialEmail;
    }

    public void setInitialEmail(String initialEmail) {
        this.initialEmail = initialEmail;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean isEmailUpdated() {
        return emailUpdated;
    }

    public void setEmailUpdated(boolean emailUpdated) {
        this.emailUpdated = emailUpdated;
    }

    public MemberExtData getMemberData() {
        return memberData;
    }

    public void setMemberData(MemberExtData memberData) {
        this.memberData = memberData;
    }

    public UnionEnrollmentData getUnionEnrollmentData() {
        return unionEnrollmentData;
    }

    public void setUnionEnrollmentData(UnionEnrollmentData unionEnrollmentData) {
        this.unionEnrollmentData = unionEnrollmentData;
    }

    public WelfareData getWelfareData() {
        return welfareData;
    }

    public void setWelfareData(WelfareData welfareData) {
        this.welfareData = welfareData;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMemberId() {
        return memberId;
    }

    public UserInfo getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserInfo selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public boolean getAs400connection(){
        return ConnectionFactory.canConnect();
    }
}
