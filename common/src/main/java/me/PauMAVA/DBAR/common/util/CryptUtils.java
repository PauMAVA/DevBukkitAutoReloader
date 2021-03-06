package me.PauMAVA.DBAR.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptUtils {

    public static String sha256(String text) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
