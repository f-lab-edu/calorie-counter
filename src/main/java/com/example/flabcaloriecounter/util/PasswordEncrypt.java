package com.example.flabcaloriecounter.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordEncrypt {

    private PasswordEncrypt() {
    }

    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isMatch(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
