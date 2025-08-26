package com.banking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    
    private Account account;
    
    @BeforeEach
    void setUp() {
        // Create a concrete instance of Account for testing
        account = new Account("TEST123") {};
    }
    
    @Test
    void testAccountCreation() {
        assertEquals("TEST123", account.getAccountNumber());
        assertEquals(0.0, account.getBalance(), 0.01);
        assertNotNull(account.getTransactionList());
        assertTrue(account.getTransactionList().isEmpty());
        assertNotNull(account.getCreatedAt());
    }
    
    @Test
    void testDepositPositiveAmount() {
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance(), 0.01);
        
        // Check that transaction was recorded
        assertEquals(1, account.getTransactionList().size());
        Transaction transaction = account.getTransactionList().get(0);
        assertEquals("deposit", transaction.getType());
        assertEquals(100.0, transaction.getAmount(), 0.01);
        assertEquals(100.0, transaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testDepositZeroAmount() {
        account.deposit(0.0);
        assertEquals(0.0, account.getBalance(), 0.01);
        assertTrue(account.getTransactionList().isEmpty());
    }
    
    @Test
    void testDepositNegativeAmount() {
        account.deposit(-50.0);
        assertEquals(0.0, account.getBalance(), 0.01);
        assertTrue(account.getTransactionList().isEmpty());
    }
    
    @Test
    void testMultipleDeposits() {
        account.deposit(100.0);
        account.deposit(50.0);
        account.deposit(25.0);
        
        assertEquals(175.0, account.getBalance(), 0.01);
        assertEquals(3, account.getTransactionList().size());
    }
    
    @Test
    void testWithdrawSuccessfully() {
        account.deposit(200.0);
        boolean result = account.withdraw(100.0);
        
        assertTrue(result);
        assertEquals(100.0, account.getBalance(), 0.01);
        
        // Check that withdrawal transaction was recorded
        assertEquals(2, account.getTransactionList().size());
        Transaction withdrawalTransaction = account.getTransactionList().get(1);
        assertEquals("withdrawal", withdrawalTransaction.getType());
        assertEquals(100.0, withdrawalTransaction.getAmount(), 0.01);
        assertEquals(100.0, withdrawalTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testWithdrawInsufficientBalance() {
        account.deposit(50.0);
        boolean result = account.withdraw(100.0);
        
        assertFalse(result);
        assertEquals(50.0, account.getBalance(), 0.01);
        assertEquals(1, account.getTransactionList().size()); // Only deposit transaction
    }
    
    @Test
    void testWithdrawZeroAmount() {
        account.deposit(100.0);
        boolean result = account.withdraw(0.0);
        
        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.01);
        assertEquals(1, account.getTransactionList().size()); // Only deposit transaction
    }
    
    @Test
    void testWithdrawNegativeAmount() {
        account.deposit(100.0);
        boolean result = account.withdraw(-50.0);
        
        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.01);
        assertEquals(1, account.getTransactionList().size()); // Only deposit transaction
    }
    
    @Test
    void testTransactionHistory() {
        account.deposit(1000.0);
        account.withdraw(100.0);
        account.deposit(50.0);
        account.withdraw(200.0);
        
        assertEquals(850.0, account.getBalance(), 0.01);
        assertEquals(4, account.getTransactionList().size());
        
        // Verify transaction order and details
        assertTransaction(account.getTransactionList().get(0), "deposit", 1000.0, 1000.0);
        assertTransaction(account.getTransactionList().get(1), "withdrawal", 100.0, 900.0);
        assertTransaction(account.getTransactionList().get(2), "deposit", 50.0, 950.0);
        assertTransaction(account.getTransactionList().get(3), "withdrawal", 200.0, 750.0);
    }
    
    private void assertTransaction(Transaction transaction, String expectedType, 
                                 double expectedAmount, double expectedBalance) {
        assertEquals(expectedType, transaction.getType());
        assertEquals(expectedAmount, transaction.getAmount(), 0.01);
        assertEquals(expectedBalance, transaction.getResultingBalance(), 0.01);
    }
}
