package com.banking.model;

import java.io.Serial;

/**
 * CheckingAccount represents a checking bank account.
 * It extends Account and optionally allows overdraft (negative balance).
 */
public class CheckingAccount extends Account {
    @Serial
    private static final long serialVersionUID = 1L;

    // Overdraft limit allowed; how far below zero the balance can go
    private double overdraftLimit;

    /**
     * Constructor to create a CheckingAccount with account number and overdraft limit.
     *
     * @param accountNumber Unique account identifier
     * @param overdraftLimit Maximum overdraft allowed (e.g., 500 means balance can go down to -500)
     */
    public CheckingAccount(String accountNumber, double overdraftLimit) {
        super(accountNumber);  // Call superclass constructor
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * Get the overdraft limit.
     *
     * @return The overdraft limit amount
     */
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     * Set a new overdraft limit.
     *
     * @param overdraftLimit New overdraft limit amount
     */
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * Override withdraw method to allow overdraft up to the limit.
     *
     * @param amount The amount to withdraw
     */
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if ((balance - amount) >= -overdraftLimit) {
            balance -= amount;
            transactionList.add(new Transaction("withdrawal", amount, balance));
            return true;
        }
        System.out.println("Withdrawal denied: Overdraft limit exceeded.");
        return false;
    }
}
