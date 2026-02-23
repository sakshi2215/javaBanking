package com.sakshi.banking.utility;
import java.security.SecureRandom;

/*
Fixed length ( 12 digits)
Bank prefix
Unique customer sequence
Checksum digit (for validation)
Hard to guess
* */




public class AccountNumberGenerationUtil {

    private static final String BANK_CODE = "12"; // your bank code
    private static final int CORE_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNumber() {
        String core = generateRandomDigits(CORE_LENGTH);
        String partial = BANK_CODE + core;
        int checksum = calculateLuhnChecksum(partial);
        return partial + checksum;
    }

    private static String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static int calculateLuhnChecksum(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));

            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }

            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }

    public static void main(String[] args) {
        System.out.println(generateAccountNumber());
    }
}
