package com.banking.model;

import java.io.Serial;

/**
 * SavingsAccount class represents a savings bank account.
 * It extends the abstract Account class and adds an interest rate feature.
 */
public class SavingsAccount extends Account {
    @Serial
    private static final long serialVersionUID = 1L;

    // Interest rate for this savings account, e.g., 0.05 for 5%
    private double interestRate;

    /**
     * Constructor to create a SavingsAccount with an account number and interest rate.
     *
     * @param accountNumber Unique identifier for the account
     * @param interestRate  Interest rate for the savings account (decimal form, e.g., 0.05 for 5%)
     */
    public SavingsAccount(String accountNumber, double interestRate) {
        super(accountNumber);  // Call the constructor of the base class Account
        this.interestRate = interestRate;
    }

    /**
     * Get the current interest rate.
     *
     * @return Interest rate as a decimal (e.g., 0.05 for 5%)
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Set a new interest rate for the account.
     *
     * @param interestRate New interest rate (decimal form)
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Apply interest to the current balance based on the interest rate.
     * This method increases the balance and records a deposit transaction for the earned interest.
     */
    public void applyInterest() {
        double interest = balance * interestRate;  // Calculate interest amount
        deposit(interest);  // Use deposit method to add interest and record transaction
    }
}
