package org.uftwf.account.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.uftwf.account.service.MySqlService;

import javax.validation.ConstraintViolation;

import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by xyang on 4/28/17.
 */
public class UserUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserUpdate model = (UserUpdate) target;
        String username = model.getUser().getUsername();
        String firstname = model.getUser().getFirstname();
        String lastname = model.getUser().getLastname();
        String zipCode = model.getUser().getZipCode();
        String email = model.getUser().getEmail();
        String phoneOption = model.getOptin();
        String phonenumber = model.getPhoneField();
//        String password=model.getPassword();
        String newpassword = model.getNewpassword();
        String confirmpassword = model.getConfirmpassword();

        if (!this.validateName(firstname)) {
            errors.rejectValue("user.firstname", "", "Invalid format for first name.");
        }
        if (!this.validateName(lastname)) {
            errors.rejectValue("user.lastname", "", "Invalid format for last name.");
        }
        if (!this.validateEmail(email)) {
            errors.rejectValue("user.email", "", "Invalid email format or email from a forbidden domain");
        }
        if (zipCode!=null&&zipCode.length()>0&&!this.validateZip(zipCode)) {
            errors.rejectValue("user.zipCode", "", "Invalid zip code format.");
        }
//        if(!this.validatePassword(password)){
//            errors.rejectValue("Password","","Invalid Password");
//        }
        if (!this.validatePassword(newpassword)) {
            errors.rejectValue("Newpassword", "", "Invalid Password format. Password must be at least 8 characters follow by minimum one uppercase, one lowercase and one digit.");
        }
        if (!this.validateConfirmPassword(confirmpassword, newpassword)) {
            errors.rejectValue("Confirmpassword", "", "Confirm Password not match with Change Password");
        }
        if (!this.validPhone(phonenumber, phoneOption)) {
            errors.rejectValue("phoneField", "", "Invalid phone number format.");
        }

    }

    public boolean validateConfirmPassword(String confirmPassword, String newPassword) {
        boolean valid = true;
        if (newPassword == null || newPassword.length() <= 0) {
            if (confirmPassword == null || confirmPassword.length() <= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (!confirmPassword.equalsIgnoreCase(newPassword)) {
                return false;
            }
            return true;
        }
    }

    //    public boolean validateNewPassword(String newPassword,String password){
//        if(password==null||password.length()<=0){
//            if(newPassword==null||newPassword.length()<=0){
//                return true;
//            }
//            else{
//                return false;
//            }
//        }
//        else{
//            return validatePassword(newPassword);
//        }
//    }
    public boolean validatePassword(String password) {
        boolean valid = true;
        if (password == null || password.length() <= 0) {
            return true;
        }
        if (password.length() < 8) {
            valid = false;
        } else {
            String pattern = "^(?=.*\\d)(?=.*[A-Z])[a-zA-Z0-9\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{8,}$";
            valid = password.matches(pattern);
        }
        return valid;
    }

    public boolean validateZip(String zipCode) {
        boolean valid = true;
        if (zipCode == null || zipCode.length() != 5) {
            valid = false;
        } else {
            String pattern = "^\\d+$";
            valid = zipCode.matches(pattern);
        }
        return valid;
    }

    public boolean validateEmail(String email) {
        boolean valid = true;
        if (email == null || email.length() < 1) {
            valid = false;
        } else {
            ArrayList<String> forbiddenDomain = MySqlService.getInstance().getForbiddenDomain();
            String pattern = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            if (!email.matches(pattern)) {
                return false;
            }
            for (String domain : forbiddenDomain) {
                if (email.indexOf(domain) > 1) {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    public boolean validateName(String name) {
        boolean valid = true;
        if (name == null || name.length() < 1) {
            valid = false;
        } else {
            String pattern = "^([a-zA-Z]+[\\.\\-'\\s]?)+[a-zA-Z]*$";

            valid = name.matches(pattern);
        }
        return valid;
    }



    public boolean validPhone(String phone, String phoneOption) {
        boolean valid = true;
        if (phoneOption != null && phoneOption.equalsIgnoreCase("yes")) {
            if (phone == null || phone.length() <= 0 || phone.equalsIgnoreCase("--")) {
                return false;
            }
        } else {
            if (phone == null || phone.length() <= 0 || phone.equalsIgnoreCase("--")) {
                return true;
            }

            String pattern = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$";
            valid = phone.matches(pattern);
        }
        return valid;
    }

}
