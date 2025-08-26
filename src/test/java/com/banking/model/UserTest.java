package com.banking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    
    private User user;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "TestPassword123";
    
    @BeforeEach
    void setUp() {
        user = new User(USERNAME, PASSWORD);
    }
    
    @Test
    void testUserCreation() {
        assertEquals(USERNAME, user.getUsername());
        assertNotNull(user);
    }
    
    @Test
    void testGetUsername() {
        assertEquals(USERNAME, user.getUsername());
    }
    
    @Test
    void testSuccessfulAuthentication() {
        assertTrue(user.authenticate(PASSWORD));
    }
    
    @Test
    void testFailedAuthenticationWithWrongPassword() {
        assertFalse(user.authenticate("WrongPassword123"));
    }
    
    @Test
    void testFailedAuthenticationWithNullPassword() {
        assertFalse(user.authenticate(null));
    }
    
    @Test
    void testFailedAuthenticationWithEmptyPassword() {
        assertFalse(user.authenticate(""));
    }
    
    @Test
    void testDifferentUsers() {
        User user1 = new User("user1", "Password1");
        User user2 = new User("user2", "Password2");
        
        assertEquals("user1", user1.getUsername());
        assertEquals("user2", user2.getUsername());
        
        assertTrue(user1.authenticate("Password1"));
        assertTrue(user2.authenticate("Password2"));
        
        assertFalse(user1.authenticate("Password2"));
        assertFalse(user2.authenticate("Password1"));
    }
    
    @Test
    void testPasswordValidation() {
        // Test with various password combinations
        assertTrue(user.authenticate("TestPassword123"));
        assertFalse(user.authenticate("testpassword123")); // Different case
        assertFalse(user.authenticate("TestPassword124")); // Different number
        assertFalse(user.authenticate("TestPassword12")); // Missing character
    }
    
    @Test
    void testConstructorWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "password");
        });
    }
    
    @Test
    void testConstructorWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "password");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new User("   ", "password");
        });
    }
    
    @Test
    void testConstructorWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("username", null);
        });
    }
    
    @Test
    void testValidUsernameAndPassword() {
        User validUser = new User("validuser", "ValidPass123");
        assertEquals("validuser", validUser.getUsername());
        assertTrue(validUser.authenticate("ValidPass123"));
    }
    
    @Test
    void testSpecialCharactersInUsername() {
        // Test with special characters in username
        User specialUser = new User("user@test.com", "Password123");
        assertEquals("user@test.com", specialUser.getUsername());
        assertTrue(specialUser.authenticate("Password123"));
    }
    
    @Test
    void testCaseSensitiveAuthentication() {
        User caseUser = new User("caseuser", "CaseSensitive123");
        
        // Original password should work
        assertTrue(caseUser.authenticate("CaseSensitive123"));
        
        // Different cases should not work
        assertFalse(caseUser.authenticate("casesensitive123"));
        assertFalse(caseUser.authenticate("CASESENSITIVE123"));
        assertFalse(caseUser.authenticate("caseSensitive123"));
    }
    
    @Test
    void testMultipleAuthenticationAttempts() {
        // Test that authentication is consistent
        for (int i = 0; i < 5; i++) {
            assertTrue(user.authenticate(PASSWORD));
            assertFalse(user.authenticate("WrongPassword"));
        }
    }
    
    @Test
    void testUserImmutability() {
        String originalUsername = user.getUsername();
        
        // Create another user to verify it doesn't affect the original user
        User anotherUser = new User("anotheruser", "AnotherPass123");
        
        // Verify the anotherUser was created correctly
        assertEquals("anotheruser", anotherUser.getUsername());
        assertTrue(anotherUser.authenticate("AnotherPass123"));
        
        // Original user should remain unchanged
        assertEquals(originalUsername, user.getUsername());
        assertTrue(user.authenticate(PASSWORD));
    }
    
    @Test
    void testLongPassword() {
        String longPassword = "VeryLongPasswordWithManyCharacters123456789";
        User longPassUser = new User("longpassuser", longPassword);
        
        assertTrue(longPassUser.authenticate(longPassword));
        assertFalse(longPassUser.authenticate(longPassword + "extra"));
    }
    
    @Test
    void testShortPassword() {
        String shortPassword = "Abc123";
        User shortPassUser = new User("shortpassuser", shortPassword);
        
        assertTrue(shortPassUser.authenticate(shortPassword));
        assertFalse(shortPassUser.authenticate(shortPassword + "extra"));
    }
    
    @Test
    void testSpecialCharactersInPassword() {
        String specialPassword = "Pass@word#123$";
        User specialPassUser = new User("specialpassuser", specialPassword);
        
        assertTrue(specialPassUser.authenticate(specialPassword));
        assertFalse(specialPassUser.authenticate("Pass@word#123")); // Missing $
    }
    
    @Test
    void testUnicodeCharacters() {
        String unicodePassword = "Pässwörd123";
        User unicodeUser = new User("unicodeuser", unicodePassword);
        
        assertTrue(unicodeUser.authenticate(unicodePassword));
        assertFalse(unicodeUser.authenticate("Passwrd123")); // Without umlauts
    }
}
