package org.uftwf.account.model;


import java.util.Objects;

/**
 * Created by xyang on 1/30/19.
 */
public class SuggestMemberApp {
    private String appName;
    private String appUrl;
    private int priority;
    private String description;
    private boolean status;
    private boolean externalApp;
    public SuggestMemberApp(String appName,String appUrl,int priority,String description,boolean status,boolean external){
        this.appName=appName;
        this.appUrl=appUrl;
        this.priority=priority;
        this.description=description;
        this.status=status;
        this.externalApp=external;
    }


    public boolean isExternalApp() {
        return externalApp;
    }

    public void setExternalApp(boolean externalApp) {
        this.externalApp = externalApp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public boolean equals(Object obj){
        SuggestMemberApp app=(SuggestMemberApp) obj;
        return app.appName.equals(appName)&&app.appUrl.equals(appUrl);
    }
    @Override
    public int hashCode(){
        return Objects.hash(appName,appUrl);
    }
}
