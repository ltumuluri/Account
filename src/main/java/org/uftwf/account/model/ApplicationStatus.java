package org.uftwf.account.model;

/**
 * Created by xyang on 1/31/19.
 */
public class ApplicationStatus {
    private String applicationName;
    private boolean enrolled;
    private String currentStatus;
    private String enrollmentDate;
    public ApplicationStatus(boolean enrolled,String currentStatus, String enrollmentDate,String applicationName){
        this.enrolled=enrolled;
        this.currentStatus=currentStatus;
        this.enrollmentDate=enrollmentDate;
        this.applicationName=applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
