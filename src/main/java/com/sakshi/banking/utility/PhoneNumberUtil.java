package com.sakshi.banking.utility;

public class PhoneNumberUtil {

    public static String formatIndianNumber(String phone) {

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        // Remove everything except digits
        phone = phone.replaceAll("\\D", "");

        // Remove leading 91 if present
        if (phone.startsWith("91") && phone.length() > 10) {
            phone = phone.substring(2);
        }

        // Remove leading 0
        if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }

        // Validate length
        if (phone.length() != 10) {
            throw new IllegalArgumentException("Invalid Indian phone number");
        }

        return "+91" + phone;
    }
}