package com.example.srin.warisanbudaya.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SRIN on 4/21/2016.
 */
public class Validator {
    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password
    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 8) {
            return true;
        }
        return false;
    }

    public boolean isValidName(String name){
//        String NAME_PATTERN = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}";
        String NAME_PATTERN = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }



    public boolean isValidPhone(String phone){
        String PHONE_PATTERN = "^\\+?\\d+$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean checkPassword(String pass1, String pass2){
        return pass1.equals(pass2);
    }
}
