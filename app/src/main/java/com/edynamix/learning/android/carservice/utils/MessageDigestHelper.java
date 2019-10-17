package com.edynamix.learning.android.carservice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestHelper {

    public static byte[] getHashForString(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(string.getBytes());
    }
}
