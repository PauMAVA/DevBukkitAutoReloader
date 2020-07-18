package me.PauMAVA.DBAR.util;

import java.util.Random;

public class RandomUtils {

    private static final String alphaNumericCharset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String randomLengthString(int min, int max) {
        int rnd = randomIntegerInRange(min, max);
        char[] arr = new char[rnd];
        for (int i = 0; i < rnd; i++) {
            arr[i] = getRandomAlphaNumeric();
        }
        return new String(arr);
    }

    public static char getRandomAlphaNumeric() {
        return alphaNumericCharset.charAt(
                randomIntegerInRange(0, alphaNumericCharset.length() - 1)
        );
    }

    public static int randomIntegerInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

}
