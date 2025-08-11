package com.banking.model;

import java.io.Serial;
import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Locale;

/**
 * The Transaction class models a banking transaction.
 * It records details about deposits or withdrawals on an account,
 * including the type, amount, timestamp, and resulting balance.
 */
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Unique identifier for each transaction
    private String transactionId;

    // Timestamp when the transaction was created
    private LocalDateTime date;

    // Type of transaction - either "deposit" or "withdrawal"
    private String type;

    // Amount of money involved in the transaction
    private double amount;

    // Account balance immediately after this transaction
    private double resultingBalance;

    // Currency formatter for Indian Rupees
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

    /**
     * Constructor to create a new Transaction object.
     * Generates a unique ID and captures the current time automatically.
     *
     * @param type             The type of transaction ("deposit" or "withdrawal")
     * @param amount           The amount involved in the transaction
     * @param resultingBalance The account balance after the transaction
     */
    public Transaction(String type, double amount, double resultingBalance) {
        // Generate a unique transaction ID using UUID
        this.transactionId = UUID.randomUUID().toString();

        // Capture the exact time when the transaction is created
        this.date = LocalDateTime.now();

        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
    }

    // Getter methods to access transaction details

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getResultingBalance() {
        return resultingBalance;
    }

    /**
     * Convenience method that returns a textual summary of the transaction,
     * including date, type, amount, and resulting balance.
     *
     * @return A descriptive string of the transaction details
     */
    public String getDescription() {
        return date.toString() + " - " + type.toUpperCase() + ": " + INR.format(amount) + ", Balance: " + INR.format(resultingBalance);
    }
}
