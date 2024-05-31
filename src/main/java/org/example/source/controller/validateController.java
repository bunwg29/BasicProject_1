package org.example.source.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This is class will regex technical to check input of user
public class validateController {

    // Check username of User if containing alpha and number will accept
    public static boolean isAlphanumeric(String username) {
        return username.matches("^[a-zA-Z0-9]+$");
    }

    // Check email if it last character is @gmail.com will accept
    public static boolean isValidGmail(String email) {
        String regex = ".+@gmail\\.com$";
        return Pattern.matches(regex, email);
    }

    // Check password if input like: Bun123@. It will accept.
    public static boolean checkPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


}
