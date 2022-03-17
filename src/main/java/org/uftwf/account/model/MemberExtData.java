package org.uftwf.account.model;

/**
 * Created by xyang on 1/31/19.
 */
public class MemberExtData {
    private String memberGroup;
    private String lastName;
    private String firstName;
    private String ssn;
    private String country;
    private String zip;
    private String wfCovered;
    private String nonmember;
    private String union_eligible;
    private String certificate_eligible;
    private String wf_date;
    private String enrollment_date;
    public MemberExtData(String memberGroup, String lastName,String firstName,String ssn,String country,String zip,String wfCovered,String nonmember,String union_eligible,String certificate_eligible,String wf_date,String enrollment_date){
        this.memberGroup=memberGroup;
        this.lastName=lastName;
        this.firstName=firstName;
        this.ssn=ssn;
        this.country=country;
        this.zip=zip;
        this.wfCovered=wfCovered;
        this.nonmember=nonmember;
        this.union_eligible=union_eligible;
        this.certificate_eligible=certificate_eligible;
        this.wf_date=wf_date;
        this.enrollment_date=enrollment_date;
    }

    public String getUnion_eligible() {
        return union_eligible;
    }

    public void setUnion_eligible(String union_eligible) {
        this.union_eligible = union_eligible;
    }

    public String getCertificate_eligible() {
        return certificate_eligible;
    }

    public void setCertificate_eligible(String certificate_eligible) {
        this.certificate_eligible = certificate_eligible;
    }

    public String getWf_date() {
        return wf_date;
    }

    public void setWf_date(String wf_date) {
        this.wf_date = wf_date;
    }

    public String getEnrollment_date() {
        return enrollment_date;
    }

    public void setEnrollment_date(String enrollment_date) {
        this.enrollment_date = enrollment_date;
    }

    public String getNonmember() {
        return nonmember;
    }

    public void setNonmember(String nonmember) {
        this.nonmember = nonmember;
    }

    public String getMemberGroup() {
        return memberGroup;
    }

    public void setMemberGroup(String memberGroup) {
        this.memberGroup = memberGroup;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getWfCovered() {
        return wfCovered;
    }

    public void setWfCovered(String wfCovered) {
        this.wfCovered = wfCovered;
    }
}
