package com.banking.services;

import com.banking.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * A simple authentication service to register users and authenticate logins.
 */
public class AuthenticationService {
    // In-memory storage of users (username mapped to User object)
    private Map<String, User> users = new HashMap<>();

    // Simple login attempt tracking
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final int maxLoginAttempts = 5;

    /**
     * Register a new user.
     * @param username The username for the new user
     * @param password The user’s password
     * @return true if successful, false if username already exists
     */
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;  // User already exists
        }
        if (!isPasswordStrong(password)) {
            return false; // Enforce simple strength rules
        }
        users.put(username, new User(username, password));
        return true;
    }

    /**
     * Authenticate a user login.
     * @param username Username entered
     * @param password Password entered
     * @return User object if authentication succeeds; null otherwise
     */
    public User login(String username, String password) {
        // check lockout
        if (loginAttempts.getOrDefault(username, 0) >= maxLoginAttempts) {
            return null;
        }
        User user = users.get(username);
        if (user != null && user.authenticate(password)) {
            loginAttempts.remove(username);
            return user;
        }
        loginAttempts.merge(username, 1, Integer::sum);
        return null;  // Authentication failed
    }

    /**
     * Retrieve the User object by username, or null if not found.
     */
    public User getUser(String username) {
        return users.get(username);
    }

    private boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        return hasUpper && hasLower && hasDigit;
    }

    // Persistence helpers
    public Map<String, User> exportUsers() {
        return new HashMap<>(users);
    }

    public void importUsers(Map<String, User> imported) {
        users.clear();
        if (imported != null) {
            users.putAll(imported);
        }
        loginAttempts.clear();
    }
}
