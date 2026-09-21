package com.fluffb4ll.lct112HttpBackend.util;

import java.util.regex.Pattern;

public class RegexValidator {
    // TODO: тянуть длины из конфигов?
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");
    private static final Pattern NICKNAME_PATTERN =
            Pattern.compile("^[A-Za-z_.\\d-]{1,16}$");
    private static final Pattern FULLNAME_PATTERN =
            Pattern.compile("^[A-Za-zА-Яа-я ']{1,150}$");

    public static boolean isNotAValidPassword(String rawPassword) {
        if (rawPassword == null)
            return true;
        return !PASSWORD_PATTERN.matcher(rawPassword).matches();
    }

    public static boolean isNotAValidUsername(String username) {
        if (username == null)
            return true;
        return !NICKNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isNotAValidFullName(String fullName) {
        if (fullName == null)
            return true;
        return !FULLNAME_PATTERN.matcher(fullName).matches();
    }
}
