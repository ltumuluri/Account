package org.uftwf.account.model;

import org.uftwf.ssoclient.model.KeycloakUser;

/**
 * Created by xyang on 1/28/19.
 */
public class UserInfo {
    private KeycloakUser user;
    private String memberId;
    private boolean optin;
    private String optInNumber;
    private boolean isMember;
    private boolean isCCP;
    private boolean isEmailOptOut;
    private String unionStatus;
    private String titleId;

    public String getUnionStatus() {
        return unionStatus;
    }

    public void setUnionStatus(String unionStatus) {
        this.unionStatus = unionStatus;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public boolean isEmailOptOut() {
        return isEmailOptOut;
    }

    public void setEmailOptOut(boolean emailOptOut) {
        isEmailOptOut = emailOptOut;
    }

    public boolean isCCP() {
        return isCCP;
    }

    public void setCCP(boolean CCP) {
        isCCP = CCP;
    }

    public boolean isOptin() {
        return optin;
    }

    public void setOptin(boolean optin) {
        this.optin = optin;
    }

    public String getOptInNumber() {
        return optInNumber;
    }

    public void setOptInNumber(String optInNumber) {
        this.optInNumber = optInNumber;
    }

    public KeycloakUser getUser() {
        return user;
    }

    public void setUser(KeycloakUser user) {
        this.user = user;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }
}
