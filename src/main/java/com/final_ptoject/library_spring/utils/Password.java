package com.final_ptoject.library_spring.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility class for hashing and salting passwords
 * @deprecated (used in servlet version of final project)
 */
@Deprecated
public class Password {
    private static final SecureRandom RANDOM = new SecureRandom();

    private Password() {
    }

    /**
     * Method to generate random salt
     * @deprecated (used in servlet version of final project)
     * @return salt byte array
     */
    @Deprecated
    public static byte[] generateSalt() {
        final byte[] salt = new byte[32];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Method to hash password with salt
     * @deprecated (used in servlet version of final project)
     * @param password raw password
     * @param salt salt byte array
     * @return hashed password byte array
     */
    @Deprecated
    public static byte[] hash(String password, byte[] salt) {
        MessageDigest digest;
        byte[] hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hash;
    }
}
