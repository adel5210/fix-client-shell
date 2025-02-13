package com.axos.clearing.fixclienttest.utils;

import quickfix.fix42.Message;

/**
 * @author Adel.Albediwy
 */
public class FixUtilz {


    private static int calculateChecksum(final String fixString) {
        int sum = 0;
        for (char c : fixString.toCharArray()) {
            sum += c;
        }
        return sum % 256;
    }

    public static int checksum42(final quickfix.fix42.Message message) {
        // Calculate and add the checksum
        final String fixString = message.toString();
        return calculateChecksum(fixString);
    }

    public static int checksum44(final quickfix.fix44.Message message) {
        // Calculate and add the checksum
        final String fixString = message.toString();
        return calculateChecksum(fixString);
    }


}
