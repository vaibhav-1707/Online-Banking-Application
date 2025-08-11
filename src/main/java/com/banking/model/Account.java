package com.banking.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Account class models a bank account.
 * It stores the account number, current balance, and the list of transactions.
 * It provides methods to deposit money, withdraw money, and get the balance.
 * 
 * Note: This is an abstract class because we expect to have different types of accounts
 * (like SavingsAccount or CheckingAccount) that will extend this base class.
 */
public abstract class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Unique account number to identify each account
    protected String accountNumber;

    // Current balance of the account
    protected double balance;

    // List of all transactions performed on this account
    protected List<Transaction> transactionList;

    // Creation timestamp for the account
    protected LocalDateTime createdAt;

    /**
     * Constructor to create a new Account with a given account number.
     * Initializes the balance to zero and creates an empty list for transactions.
     * 
     * @param accountNumber A unique identifier for this account
     */
    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;  // Account starts with zero balance
        this.transactionList = new ArrayList<>();  // No transactions at the beginning
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Get the account number.
     * 
     * @return The unique account number as a String
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Get the current balance of the account.
     * 
     * @return The current balance as a double
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Get the list of all past transactions for this account.
     * 
     * @return A List of Transaction objects
     */
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    /**
     * Get the account creation timestamp.
     * @return LocalDateTime when the account was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Deposit a positive amount into the account.
     * Updates the balance and records the transaction.
     * 
     * @param amount The amount to deposit (must be > 0)
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;  // Add amount to balance
            // Record this deposit transaction
            transactionList.add(new Transaction("deposit", amount, balance));
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraw a positive amount from the account if sufficient balance exists.
     * Updates the balance and records the transaction.
     * 
     * @param amount The amount to withdraw (must be > 0 and <= balance)
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (balance >= amount) {
            balance -= amount;  // Deduct amount from balance
            // Record this withdrawal transaction
            transactionList.add(new Transaction("withdrawal", amount, balance));
            return true;
        }
        System.out.println("Insufficient balance.");
        return false;
    }
}
