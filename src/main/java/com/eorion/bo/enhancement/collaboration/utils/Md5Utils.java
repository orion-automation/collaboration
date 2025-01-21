package com.eorion.bo.enhancement.collaboration.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Utils {
    public static String getMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] b = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xff);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
