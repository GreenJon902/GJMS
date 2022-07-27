package com.greenjon902.gjms;

/**
 * The tools that a lot of tests will require
 */
public class Utils {
    public static byte[] byteArray(int... arr) {
        byte[] byteArray = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            byteArray[i] = (byte) arr[i];
        }
        return byteArray;
    }
}
