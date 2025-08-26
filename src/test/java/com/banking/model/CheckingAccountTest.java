package com.banking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckingAccountTest {
    
    private CheckingAccount checkingAccount;
    private static final double OVERDRAFT_LIMIT = 500.0;
    
    @BeforeEach
    void setUp() {
        checkingAccount = new CheckingAccount("CHECKING123", OVERDRAFT_LIMIT);
    }
    
    @Test
    void testCheckingAccountCreation() {
        assertEquals("CHECKING123", checkingAccount.getAccountNumber());
        assertEquals(0.0, checkingAccount.getBalance(), 0.01);
        assertEquals(OVERDRAFT_LIMIT, checkingAccount.getOverdraftLimit(), 0.01);
        assertNotNull(checkingAccount.getTransactionList());
        assertTrue(checkingAccount.getTransactionList().isEmpty());
    }
    
    @Test
    void testGetOverdraftLimit() {
        assertEquals(OVERDRAFT_LIMIT, checkingAccount.getOverdraftLimit(), 0.01);
    }
    
    @Test
    void testSetOverdraftLimit() {
        double newLimit = 1000.0;
        checkingAccount.setOverdraftLimit(newLimit);
        assertEquals(newLimit, checkingAccount.getOverdraftLimit(), 0.01);
    }
    
    @Test
    void testWithdrawWithinBalance() {
        checkingAccount.deposit(1000.0);
        boolean result = checkingAccount.withdraw(500.0);
        
        assertTrue(result);
        assertEquals(500.0, checkingAccount.getBalance(), 0.01);
        assertEquals(2, checkingAccount.getTransactionList().size());
        
        Transaction withdrawalTransaction = checkingAccount.getTransactionList().get(1);
        assertEquals("withdrawal", withdrawalTransaction.getType());
        assertEquals(500.0, withdrawalTransaction.getAmount(), 0.01);
        assertEquals(500.0, withdrawalTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testWithdrawUsingOverdraft() {
        checkingAccount.deposit(100.0);
        boolean result = checkingAccount.withdraw(400.0); // 300 over balance, within overdraft limit
        
        assertTrue(result);
        assertEquals(-300.0, checkingAccount.getBalance(), 0.01);
        assertEquals(2, checkingAccount.getTransactionList().size());
        
        Transaction withdrawalTransaction = checkingAccount.getTransactionList().get(1);
        assertEquals("withdrawal", withdrawalTransaction.getType());
        assertEquals(400.0, withdrawalTransaction.getAmount(), 0.01);
        assertEquals(-300.0, withdrawalTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testWithdrawExceedingOverdraftLimit() {
        checkingAccount.deposit(100.0);
        boolean result = checkingAccount.withdraw(700.0); // 600 over balance, exceeds overdraft limit of 500
        
        assertFalse(result);
        assertEquals(100.0, checkingAccount.getBalance(), 0.01);
        assertEquals(1, checkingAccount.getTransactionList().size()); // Only deposit transaction
    }
    
    @Test
    void testWithdrawExactOverdraftLimit() {
        boolean result = checkingAccount.withdraw(OVERDRAFT_LIMIT); // Exactly at overdraft limit
        
        assertTrue(result);
        assertEquals(-OVERDRAFT_LIMIT, checkingAccount.getBalance(), 0.01);
        assertEquals(1, checkingAccount.getTransactionList().size());
        
        Transaction withdrawalTransaction = checkingAccount.getTransactionList().get(0);
        assertEquals("withdrawal", withdrawalTransaction.getType());
        assertEquals(OVERDRAFT_LIMIT, withdrawalTransaction.getAmount(), 0.01);
        assertEquals(-OVERDRAFT_LIMIT, withdrawalTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testWithdrawZeroAmount() {
        checkingAccount.deposit(100.0);
        boolean result = checkingAccount.withdraw(0.0);
        
        assertFalse(result);
        assertEquals(100.0, checkingAccount.getBalance(), 0.01);
        assertEquals(1, checkingAccount.getTransactionList().size()); // Only deposit transaction
    }
    
    @Test
    void testWithdrawNegativeAmount() {
        checkingAccount.deposit(100.0);
        boolean result = checkingAccount.withdraw(-50.0);
        
        assertFalse(result);
        assertEquals(100.0, checkingAccount.getBalance(), 0.01);
        assertEquals(1, checkingAccount.getTransactionList().size()); // Only deposit transaction
    }
    
    @Test
    void testComplexOverdraftScenario() {
        // Start with zero balance, withdraw to overdraft, then deposit, then withdraw again
        boolean result1 = checkingAccount.withdraw(200.0);
        assertTrue(result1);
        assertEquals(-200.0, checkingAccount.getBalance(), 0.01);
        
        checkingAccount.deposit(300.0);
        assertEquals(100.0, checkingAccount.getBalance(), 0.01);
        
        boolean result2 = checkingAccount.withdraw(400.0); // Should go to -300, within overdraft limit
        assertTrue(result2);
        assertEquals(-300.0, checkingAccount.getBalance(), 0.01);
        
        boolean result3 = checkingAccount.withdraw(300.0); // Should go to -600, exceeds overdraft limit
        assertFalse(result3);
        assertEquals(-300.0, checkingAccount.getBalance(), 0.01);
        
        assertEquals(3, checkingAccount.getTransactionList().size());
    }
    
    @Test
    void testInheritanceFromAccount() {
        // Test that CheckingAccount inherits basic Account functionality
        checkingAccount.deposit(500.0);
        assertEquals(500.0, checkingAccount.getBalance(), 0.01);
        
        boolean withdrawResult = checkingAccount.withdraw(200.0);
        assertTrue(withdrawResult);
        assertEquals(300.0, checkingAccount.getBalance(), 0.01);
        
        assertEquals(2, checkingAccount.getTransactionList().size());
    }
    
    @Test
    void testDifferentOverdraftLimits() {
        // Test with a different overdraft limit
        CheckingAccount account2 = new CheckingAccount("CHECKING456", 1000.0);
        boolean result = account2.withdraw(800.0);
        
        assertTrue(result);
        assertEquals(-800.0, account2.getBalance(), 0.01);
        
        boolean result2 = account2.withdraw(300.0); // Should exceed the 1000 limit
        assertFalse(result2);
        assertEquals(-800.0, account2.getBalance(), 0.01);
        
        assertEquals(1, account2.getTransactionList().size());
    }
}
