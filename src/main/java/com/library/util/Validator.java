package com.library.util;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern CMND_PATTERN = Pattern.compile("^(\\d{9}|\\d{12})$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern ISBN_PATTERN = Pattern.compile("^[A-Za-z0-9-]{3,17}$");

    public static boolean isValidCMND(String cmnd) {
        if (cmnd == null)
            return false;
        return CMND_PATTERN.matcher(cmnd.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidISBN(String isbn) {
        if (isbn == null)
            return false;
        return ISBN_PATTERN.matcher(isbn.trim()).matches();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isPositiveInteger(String str) {
        try {
            return Integer.parseInt(str.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String str) {
        try {
            return Double.parseDouble(str.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidYear(String str) {
        try {
            int year = Integer.parseInt(str.trim());
            int currentYear = java.time.LocalDate.now().getYear();
            return year >= 1900 && year <= currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
