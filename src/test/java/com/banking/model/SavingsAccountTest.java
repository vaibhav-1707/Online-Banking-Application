package com.banking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTest {
    
    private SavingsAccount savingsAccount;
    private static final double INTEREST_RATE = 0.05; // 5%
    
    @BeforeEach
    void setUp() {
        savingsAccount = new SavingsAccount("SAVINGS123", INTEREST_RATE);
    }
    
    @Test
    void testSavingsAccountCreation() {
        assertEquals("SAVINGS123", savingsAccount.getAccountNumber());
        assertEquals(0.0, savingsAccount.getBalance(), 0.01);
        assertEquals(INTEREST_RATE, savingsAccount.getInterestRate(), 0.01);
        assertNotNull(savingsAccount.getTransactionList());
        assertTrue(savingsAccount.getTransactionList().isEmpty());
    }
    
    @Test
    void testGetInterestRate() {
        assertEquals(0.05, savingsAccount.getInterestRate(), 0.01);
    }
    
    @Test
    void testSetInterestRate() {
        double newRate = 0.06;
        savingsAccount.setInterestRate(newRate);
        assertEquals(newRate, savingsAccount.getInterestRate(), 0.01);
    }
    
    @Test
    void testApplyInterestWithZeroBalance() {
        savingsAccount.applyInterest();
        assertEquals(0.0, savingsAccount.getBalance(), 0.01);
        assertEquals(1, savingsAccount.getTransactionList().size());
        
        Transaction interestTransaction = savingsAccount.getTransactionList().get(0);
        assertEquals("deposit", interestTransaction.getType());
        assertEquals(0.0, interestTransaction.getAmount(), 0.01);
        assertEquals(0.0, interestTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testApplyInterestWithPositiveBalance() {
        savingsAccount.deposit(1000.0);
        double expectedInterest = 1000.0 * INTEREST_RATE; // 50.0
        double expectedBalance = 1000.0 + expectedInterest; // 1050.0
        
        savingsAccount.applyInterest();
        
        assertEquals(expectedBalance, savingsAccount.getBalance(), 0.01);
        assertEquals(2, savingsAccount.getTransactionList().size());
        
        // Check the interest transaction
        Transaction interestTransaction = savingsAccount.getTransactionList().get(1);
        assertEquals("deposit", interestTransaction.getType());
        assertEquals(expectedInterest, interestTransaction.getAmount(), 0.01);
        assertEquals(expectedBalance, interestTransaction.getResultingBalance(), 0.01);
    }
    
    @Test
    void testApplyInterestMultipleTimes() {
        savingsAccount.deposit(1000.0);
        
        // First interest application
        savingsAccount.applyInterest();
        double balanceAfterFirstInterest = 1000.0 + (1000.0 * INTEREST_RATE);
        assertEquals(balanceAfterFirstInterest, savingsAccount.getBalance(), 0.01);
        
        // Second interest application
        savingsAccount.applyInterest();
        double secondInterest = balanceAfterFirstInterest * INTEREST_RATE;
        double balanceAfterSecondInterest = balanceAfterFirstInterest + secondInterest;
        assertEquals(balanceAfterSecondInterest, savingsAccount.getBalance(), 0.01);
        
        assertEquals(3, savingsAccount.getTransactionList().size());
    }
    
    @Test
    void testApplyInterestWithSmallBalance() {
        savingsAccount.deposit(100.0);
        savingsAccount.applyInterest();
        
        double expectedInterest = 100.0 * INTEREST_RATE; // 5.0
        double expectedBalance = 105.0;
        
        assertEquals(expectedBalance, savingsAccount.getBalance(), 0.01);
        
        Transaction interestTransaction = savingsAccount.getTransactionList().get(1);
        assertEquals(expectedInterest, interestTransaction.getAmount(), 0.01);
    }
    
    @Test
    void testInheritanceFromAccount() {
        // Test that SavingsAccount inherits basic Account functionality
        savingsAccount.deposit(500.0);
        assertEquals(500.0, savingsAccount.getBalance(), 0.01);
        
        boolean withdrawResult = savingsAccount.withdraw(200.0);
        assertTrue(withdrawResult);
        assertEquals(300.0, savingsAccount.getBalance(), 0.01);
        
        assertEquals(2, savingsAccount.getTransactionList().size());
    }
    
    @Test
    void testInterestRatePrecision() {
        // Test with a different interest rate
        SavingsAccount account2 = new SavingsAccount("SAVINGS456", 0.025); // 2.5%
        account2.deposit(1000.0);
        account2.applyInterest();
        
        double expectedInterest = 1000.0 * 0.025; // 25.0
        double expectedBalance = 1025.0;
        
        assertEquals(expectedBalance, account2.getBalance(), 0.01);
        
        Transaction interestTransaction = account2.getTransactionList().get(1);
        assertEquals(expectedInterest, interestTransaction.getAmount(), 0.01);
    }
}
