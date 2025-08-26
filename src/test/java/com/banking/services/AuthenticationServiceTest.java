package com.banking.services;

import com.banking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class AuthenticationServiceTest {
    
    private AuthenticationService authService;
    
    @BeforeEach
    void setUp() {
        authService = new AuthenticationService();
    }
    
    @Test
    void testSuccessfulUserRegistration() {
        boolean result = authService.registerUser("testuser", "TestPassword123");
        assertTrue(result);
        
        User user = authService.getUser("testuser");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }
    
    @Test
    void testDuplicateUserRegistration() {
        // Register first user
        boolean result1 = authService.registerUser("duplicateuser", "Password123");
        assertTrue(result1);
        
        // Try to register same username again
        boolean result2 = authService.registerUser("duplicateuser", "DifferentPassword123");
        assertFalse(result2);
    }
    
    @Test
    void testSuccessfulLogin() {
        // Register user first
        authService.registerUser("loginuser", "LoginPassword123");
        
        // Login with correct credentials
        User user = authService.login("loginuser", "LoginPassword123");
        assertNotNull(user);
        assertEquals("loginuser", user.getUsername());
    }
    
    @Test
    void testFailedLoginWithWrongPassword() {
        // Register user first
        authService.registerUser("wrongpassuser", "CorrectPassword123");
        
        // Login with wrong password
        User user = authService.login("wrongpassuser", "WrongPassword123");
        assertNull(user);
    }
    
    @Test
    void testFailedLoginWithNonExistentUser() {
        // Try to login with user that doesn't exist
        User user = authService.login("nonexistentuser", "AnyPassword123");
        assertNull(user);
    }
    
    @Test
    void testPasswordStrengthValidation() {
        // Test weak passwords
        assertFalse(authService.registerUser("user1", "weak")); // Too short
        assertFalse(authService.registerUser("user2", "password")); // No uppercase, no digit
        assertFalse(authService.registerUser("user3", "Password")); // No digit
        assertFalse(authService.registerUser("user4", "password123")); // No uppercase
        assertFalse(authService.registerUser("user5", "PASSWORD123")); // No lowercase
        
        // Test strong passwords
        assertTrue(authService.registerUser("user6", "Password123"));
        assertTrue(authService.registerUser("user7", "StrongPass456"));
        assertTrue(authService.registerUser("user8", "MySecurePass789"));
    }
    
    @Test
    void testGetUserWithValidUsername() {
        authService.registerUser("getuser", "GetUserPass123");
        
        User user = authService.getUser("getuser");
        assertNotNull(user);
        assertEquals("getuser", user.getUsername());
    }
    
    @Test
    void testGetUserWithInvalidUsername() {
        User user = authService.getUser("invaliduser");
        assertNull(user);
    }
    
    @Test
    void testMultipleUserRegistration() {
        assertTrue(authService.registerUser("user1", "Password123"));
        assertTrue(authService.registerUser("user2", "Password456"));
        assertTrue(authService.registerUser("user3", "Password789"));
        
        // Verify all users exist
        assertNotNull(authService.getUser("user1"));
        assertNotNull(authService.getUser("user2"));
        assertNotNull(authService.getUser("user3"));
        
        // Verify each user can login
        assertNotNull(authService.login("user1", "Password123"));
        assertNotNull(authService.login("user2", "Password456"));
        assertNotNull(authService.login("user3", "Password789"));
    }
    
    @Test
    void testLoginAttemptTracking() {
        // Register a user
        authService.registerUser("lockeduser", "CorrectPassword123");
        
        // Make multiple failed login attempts
        for (int i = 0; i < 5; i++) {
            User user = authService.login("lockeduser", "WrongPassword");
            assertNull(user);
        }
        
        // 6th attempt should still fail
        User user = authService.login("lockeduser", "WrongPassword");
        assertNull(user);
        
        // Correct password should also fail due to lockout
        User correctUser = authService.login("lockeduser", "CorrectPassword123");
        assertNull(correctUser);
    }
    
    @Test
    void testSuccessfulLoginResetsAttemptCounter() {
        // Register a user
        authService.registerUser("resetuser", "CorrectPassword123");
        
        // Make some failed attempts
        authService.login("resetuser", "WrongPassword");
        authService.login("resetuser", "WrongPassword");
        
        // Successful login should reset the counter
        User user = authService.login("resetuser", "CorrectPassword123");
        assertNotNull(user);
        
        // Should be able to make more failed attempts after successful login
        authService.login("resetuser", "WrongPassword");
        authService.login("resetuser", "WrongPassword");
        
        // Correct login should still work
        User user2 = authService.login("resetuser", "CorrectPassword123");
        assertNotNull(user2);
    }
    
    @Test
    void testExportUsers() {
        authService.registerUser("exportuser1", "Password123");
        authService.registerUser("exportuser2", "Password456");
        
        Map<String, User> exportedUsers = authService.exportUsers();
        
        assertEquals(2, exportedUsers.size());
        assertTrue(exportedUsers.containsKey("exportuser1"));
        assertTrue(exportedUsers.containsKey("exportuser2"));
        
        assertEquals("exportuser1", exportedUsers.get("exportuser1").getUsername());
        assertEquals("exportuser2", exportedUsers.get("exportuser2").getUsername());
    }
    
    @Test
    void testImportUsers() {
        // Create a new authentication service
        AuthenticationService newAuthService = new AuthenticationService();
        
        // Export users from original service
        authService.registerUser("importuser1", "Password123");
        authService.registerUser("importuser2", "Password456");
        Map<String, User> usersToImport = authService.exportUsers();
        
        // Import into new service
        newAuthService.importUsers(usersToImport);
        
        // Verify users were imported
        assertTrue(newAuthService.login("importuser1", "Password123") != null);
        assertTrue(newAuthService.login("importuser2", "Password456") != null);
    }
    
    @Test
    void testImportUsersWithNullMap() {
        // Should handle null gracefully
        authService.importUsers(null);
        
        // Service should still be in a valid state
        assertTrue(authService.registerUser("newuser", "Password123"));
    }
    
    @Test
    void testImportUsersClearsExistingUsers() {
        // Add some initial users
        authService.registerUser("olduser1", "Password123");
        authService.registerUser("olduser2", "Password456");
        
        // Create new users map
        AuthenticationService tempAuth = new AuthenticationService();
        tempAuth.registerUser("newuser1", "NewPassword123");
        tempAuth.registerUser("newuser2", "NewPassword456");
        Map<String, User> newUsers = tempAuth.exportUsers();
        
        // Import new users
        authService.importUsers(newUsers);
        
        // Old users should be gone
        assertNull(authService.login("olduser1", "Password123"));
        assertNull(authService.login("olduser2", "Password456"));
        
        // New users should be accessible
        assertNotNull(authService.login("newuser1", "NewPassword123"));
        assertNotNull(authService.login("newuser2", "NewPassword456"));
    }
    
    @Test
    void testCaseSensitiveUsername() {
        authService.registerUser("CaseUser", "Password123");
        
        // Different cases should be treated as different users
        assertNull(authService.login("caseuser", "Password123"));
        assertNull(authService.login("CASEUSER", "Password123"));
        assertNotNull(authService.login("CaseUser", "Password123"));
    }
    
    @Test
    void testEmptyUsernameAndPassword() {
        // Empty username should not be allowed
        assertFalse(authService.registerUser("", "Password123"));
        
        // Empty password should not be allowed (too weak)
        assertFalse(authService.registerUser("emptyuser", ""));
    }
    
    @Test
    void testSpecialCharactersInUsername() {
        assertTrue(authService.registerUser("user@test.com", "Password123"));
        assertTrue(authService.registerUser("user_name", "Password123"));
        assertTrue(authService.registerUser("user-name", "Password123"));
        
        // Verify they can login
        assertNotNull(authService.login("user@test.com", "Password123"));
        assertNotNull(authService.login("user_name", "Password123"));
        assertNotNull(authService.login("user-name", "Password123"));
    }
}
