package org.uftwf.account.model;

/**
 * Created by xyang on 1/30/19.
 */
public class UserUpdate {
    private PersonalInfo user;
    private String newpassword;
    private String confirmpassword;
    private String optin;
    private String memberId;
    private String phoneField;
    private Boolean isEmailOptOut;

    public Boolean isEmailOptOut() {
        return isEmailOptOut;
    }

    public void setEmailOptOut(Boolean emailOptOut) {
        isEmailOptOut = emailOptOut;
    }

    public String getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(String phoneField) {
        this.phoneField = phoneField;
    }

    public PersonalInfo getUser() {
        return user;
    }

    public void setUser(PersonalInfo user) {
        this.user = user;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getOptin() {
        return optin;
    }

    public void setOptin(String optin) {
        this.optin = optin;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
