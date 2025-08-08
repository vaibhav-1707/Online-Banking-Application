package com.banking.services;

import com.banking.model.User;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple authentication service to register users and authenticate logins.
 */
public class AuthenticationService {
    // In-memory storage of users (username mapped to User object)
    private Map<String, User> users = new HashMap<>();

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
        User user = users.get(username);
        if (user != null && user.authenticate(password)) {
            return user;
        }
        return null;  // Authentication failed
    }
}
