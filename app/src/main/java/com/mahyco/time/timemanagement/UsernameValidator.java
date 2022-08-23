package com.mahyco.time.timemanagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String USERNAME_PATTERN = "^[a-z0-9._-]{2,25}$";

    public UsernameValidator(){
        this.pattern = Pattern.compile(USERNAME_PATTERN);
    }

    public boolean validate(final String password){

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}