package com.hdfc.Utils;

public class MaskAccount {

    public static String maskAccount(String accountNumber) {
        System.out.println("Account number received for masking....." + accountNumber);

        if (accountNumber == null) {
            throw new IllegalArgumentException("Account number cannot be null");
        }

        // For 16 digit account numbers
        if (accountNumber.length() == 16) {
            String first4 = accountNumber.substring(0, 4);   // first 4 digits
            String last1 = accountNumber.substring(15);      // last digit
            String stars = "*".repeat(11);                   // middle 11 stars
            return first4 + stars + last1;
        }

        System.out.println("&&&&&&&&&&&&&&&&&&");
        // Invalid case
        throw new IllegalArgumentException("Invalid account number: " + accountNumber);
    }
}
