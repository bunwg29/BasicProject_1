package org.example.source.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class validateController {
    public static boolean isAlphanumeric(String username) {
        return username.matches("^[a-zA-Z0-9]+$");
    }
    public static boolean checkPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
