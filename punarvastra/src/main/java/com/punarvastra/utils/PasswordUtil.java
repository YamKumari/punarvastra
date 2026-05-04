package com.punarvastra.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * BCrypt password hashing and verification (cost factor 10).
 */
public final class PasswordUtil {

    private static final int ROUNDS = 10;

    private PasswordUtil() {
    }

    /**
     * Hashes a plaintext password for database storage.
     *
     * @param plain plaintext password
     * @return BCrypt hash string
     */
    public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(ROUNDS));
    }

    /**
     * Verifies a plaintext password against a stored BCrypt hash.
     *
     * @param plain    user input
     * @param storedHash hash from database
     * @return true if password matches
     */
    public static boolean checkPassword(String plain, String storedHash) {
        if (plain == null || storedHash == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plain, storedHash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
