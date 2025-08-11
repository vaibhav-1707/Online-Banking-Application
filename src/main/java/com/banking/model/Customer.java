package com.banking.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Customer class represents a bank customer.
 * It holds personal details, login credentials (User), and a list of accounts.
 */
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private User user;  // Associated User object for authentication
    private List<Account> accounts;

    public Customer(String name, String email, User user) {
        this.name = name;
        this.email = email;
        this.user = user;
        this.accounts = new ArrayList<>();
    }

    // Get customer's full name
    public String getName() {
        return name;
    }

    // Get customer's email address
    public String getEmail() {
        return email;
    }

    // Get the User object linked with this customer
    public User getUser() {
        return user;
    }

    // Get the list of accounts belonging to this customer
    public List<Account> getAccounts() {
        return accounts;
    }

    // Add a new bank account for this customer
    public void addAccount(Account account) {
        accounts.add(account);
    }
}
