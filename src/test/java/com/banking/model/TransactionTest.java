package com.banking.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TransactionTest {
    
    private Transaction transaction;
    private static final String TRANSACTION_TYPE = "deposit";
    private static final double AMOUNT = 1000.0;
    private static final double RESULTING_BALANCE = 1000.0;
    
    @BeforeEach
    void setUp() {
        transaction = new Transaction(TRANSACTION_TYPE, AMOUNT, RESULTING_BALANCE);
    }
    
    @Test
    void testTransactionCreation() {
        assertNotNull(transaction.getTransactionId());
        assertFalse(transaction.getTransactionId().isEmpty());
        assertEquals(TRANSACTION_TYPE, transaction.getType());
        assertEquals(AMOUNT, transaction.getAmount(), 0.01);
        assertEquals(RESULTING_BALANCE, transaction.getResultingBalance(), 0.01);
        assertNotNull(transaction.getDate());
    }
    
    @Test
    void testTransactionIdGeneration() {
        // Each transaction should have a unique ID
        Transaction transaction1 = new Transaction("deposit", 100.0, 100.0);
        Transaction transaction2 = new Transaction("deposit", 100.0, 100.0);
        
        assertNotEquals(transaction1.getTransactionId(), transaction2.getTransactionId());
        assertNotNull(transaction1.getTransactionId());
        assertNotNull(transaction2.getTransactionId());
    }
    
    @Test
    void testTransactionDateGeneration() {
        LocalDateTime beforeCreation = LocalDateTime.now();
        
        // Small delay to ensure timestamp difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Transaction newTransaction = new Transaction("withdrawal", 50.0, 50.0);
        
        LocalDateTime afterCreation = LocalDateTime.now();
        
        assertTrue(newTransaction.getDate().isAfter(beforeCreation) || 
                  newTransaction.getDate().isEqual(beforeCreation));
        assertTrue(newTransaction.getDate().isBefore(afterCreation) || 
                  newTransaction.getDate().isEqual(afterCreation));
    }
    
    @Test
    void testDifferentTransactionTypes() {
        Transaction depositTransaction = new Transaction("deposit", 500.0, 500.0);
        Transaction withdrawalTransaction = new Transaction("withdrawal", 200.0, 300.0);
        
        assertEquals("deposit", depositTransaction.getType());
        assertEquals("withdrawal", withdrawalTransaction.getType());
    }
    
    @Test
    void testTransactionWithZeroAmount() {
        Transaction zeroTransaction = new Transaction("deposit", 0.0, 0.0);
        
        assertEquals("deposit", zeroTransaction.getType());
        assertEquals(0.0, zeroTransaction.getAmount(), 0.01);
        assertEquals(0.0, zeroTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testTransactionWithNegativeAmount() {
        Transaction negativeTransaction = new Transaction("withdrawal", -50.0, -50.0);
        
        assertEquals("withdrawal", negativeTransaction.getType());
        assertEquals(-50.0, negativeTransaction.getAmount(), 0.01);
        assertEquals(-50.0, negativeTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testGetDescription() {
        String description = transaction.getDescription();
        
        assertNotNull(description);
        assertTrue(description.contains("deposit"));
        assertTrue(description.contains("₹1,000.00"));
        assertTrue(description.contains("Balance: ₹1,000.00"));
    }
    
    @Test
    void testDescriptionFormatting() {
        Transaction withdrawalTransaction = new Transaction("withdrawal", 250.75, 749.25);
        String description = withdrawalTransaction.getDescription();
        
        assertTrue(description.contains("WITHDRAWAL"));
        assertTrue(description.contains("₹250.75"));
        assertTrue(description.contains("Balance: ₹749.25"));
    }
    
    @Test
    void testDescriptionWithZeroAmount() {
        Transaction zeroTransaction = new Transaction("deposit", 0.0, 0.0);
        String description = zeroTransaction.getDescription();
        
        assertTrue(description.contains("DEPOSIT"));
        assertTrue(description.contains("₹0.00"));
        assertTrue(description.contains("Balance: ₹0.00"));
    }
    
    @Test
    void testDescriptionWithLargeAmount() {
        Transaction largeTransaction = new Transaction("deposit", 1000000.0, 1000000.0);
        String description = largeTransaction.getDescription();
        
        assertTrue(description.contains("DEPOSIT"));
        assertTrue(description.contains("₹10,00,000.00"));
        assertTrue(description.contains("Balance: ₹10,00,000.00"));
    }
    
    @Test
    void testGettersConsistency() {
        // Test that getters return the same values used in constructor
        assertEquals(TRANSACTION_TYPE, transaction.getType());
        assertEquals(AMOUNT, transaction.getAmount(), 0.01);
        assertEquals(RESULTING_BALANCE, transaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testTransactionImmutability() {
        // Test that transaction properties don't change after creation
        String originalId = transaction.getTransactionId();
        String originalType = transaction.getType();
        double originalAmount = transaction.getAmount();
        double originalBalance = transaction.getResultingBalance();
        LocalDateTime originalDate = transaction.getDate();
        
        // Create another transaction
        new Transaction("withdrawal", 500.0, 500.0);
        
        // Original transaction should remain unchanged
        assertEquals(originalId, transaction.getTransactionId());
        assertEquals(originalType, transaction.getType());
        assertEquals(originalAmount, transaction.getAmount(), 0.01);
        assertEquals(originalBalance, transaction.getResultingBalance(), 0.01);
        assertEquals(originalDate, transaction.getDate());
    }
    
    @Test
    void testMultipleTransactions() {
        Transaction[] transactions = new Transaction[5];
        
        for (int i = 0; i < 5; i++) {
            transactions[i] = new Transaction("deposit", (i + 1) * 100.0, (i + 1) * 100.0);
        }
        
        // All transactions should have unique IDs
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                assertNotEquals(transactions[i].getTransactionId(), 
                              transactions[j].getTransactionId());
            }
        }
        
        // Verify amounts
        for (int i = 0; i < 5; i++) {
            assertEquals((i + 1) * 100.0, transactions[i].getAmount(), 0.01);
            assertEquals((i + 1) * 100.0, transactions[i].getResultingBalance(), 0.01);
        }
    }
}
