package com.banking.model;

/**
 * The User class models a banking system user with credentials.
 */
public class User {
    private String username;
    private String password;  // In real apps, passwords should be hashed

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
        return this.password.equals(password);
    }
}
