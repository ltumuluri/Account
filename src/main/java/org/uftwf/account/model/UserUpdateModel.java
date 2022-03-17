package org.uftwf.account.model;

/**
 * Created by xyang on 4/28/17.
 */
public class UserUpdateModel {
    private String username;
    private String firstname;
    private String lastname;
    private String zipCode;
    private String email;

    private String newpassword;
    private String confirmpassword;

    public String getPhoneoption() {
        return phoneoption;
    }

    public void setPhoneoption(String phoneoption) {
        this.phoneoption = phoneoption;
    }

    private String phoneoption;

    public String getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(String phoneField) {
        this.phoneField = phoneField;
    }

    private String phoneField;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

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
}
