package org.uftwf.account.model;

/**
 * Created by xyang on 1/30/19.
 */
public class UnionEnrollmentData {
    private String enrollment_type_id;
    private String current_status;
    private String date_of_entry;
    public UnionEnrollmentData(String enrollment_type_id,String current_status,String date_of_entry){
        this.enrollment_type_id=enrollment_type_id;
        this.current_status=current_status;
        this.date_of_entry=date_of_entry;
    }
    public String getEnrollment_type_id() {
        return enrollment_type_id;
    }

    public void setEnrollment_type_id(String enrollment_type_id) {
        this.enrollment_type_id = enrollment_type_id;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    public String getDate_of_entry() {
        return date_of_entry;
    }

    public void setDate_of_entry(String date_of_entry) {
        this.date_of_entry = date_of_entry;
    }
}
