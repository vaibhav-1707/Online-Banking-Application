package com.banking.model;

import java.io.Serial;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The User class models a banking system user with credentials.
 * Passwords are stored using salted PBKDF2 hashing.
 */
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32; // 256-bit
    private static final int PBKDF2_ITERATIONS = 100_000;
    private static final String PBKDF2_ALG = "PBKDF2WithHmacSHA256";

    private String username;
    private byte[] passwordSalt;
    private byte[] passwordHash;

    public User(String username, String plainPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        if (plainPassword == null) {
            throw new IllegalArgumentException("password must not be null");
        }
        this.username = username;
        this.passwordSalt = generateSalt();
        this.passwordHash = hashPassword(plainPassword.toCharArray(), passwordSalt);
    }

    public String getUsername() {
        return username;
    }

    /**
     * Checks if the provided password matches this user's password.
     * @param password The password to validate
     * @return true if passwords match, else false
     */
    public boolean authenticate(String password) {
        if (password == null) return false;
        byte[] testHash = hashPassword(password.toCharArray(), passwordSalt);
        return constantTimeEquals(passwordHash, testHash);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, HASH_BYTES * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALG);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Password hashing unavailable", e);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
