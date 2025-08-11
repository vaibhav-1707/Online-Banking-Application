package com.banking.services;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * BankService handles business logic related to customers and their bank accounts.
 */
public class BankService {

    // List to store all customers
    private List<Customer> customers = new ArrayList<>();

    /**
     * Add a new customer to the bank.
     *
     * @param customer The Customer object to add
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Find a customer by their username.
     *
     * @param username The username linked with the customer
     * @return The Customer object if found, otherwise null
     */
    public Customer findCustomerByUsername(String username) {
        for (Customer customer : customers) {
            User user = customer.getUser();
            if (user != null && user.getUsername().equals(username)) {
                return customer;
            }
        }
        return null; // Customer not found
    }

    /**
     * Get all accounts of a customer identified by username.
     *
     * @param username The username of the customer
     * @return List of Account objects or empty list if no customer found
     */
    public List<Account> getAccountsForCustomer(String username) {
        Customer customer = findCustomerByUsername(username);
        if (customer != null) {
            return customer.getAccounts();
        } else {
            return new ArrayList<>(); // No accounts if customer not found
        }
    }

    /**
     * Find an account by number for a specific user.
     */
    public Account findAccount(String username, String accountNumber) {
        Customer customer = findCustomerByUsername(username);
        if (customer == null) return null;
        for (Account a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(accountNumber)) return a;
        }
        return null;
    }

    /**
     * Transfer funds between two accounts of the same user.
     * Returns true on success.
     */
    public boolean transfer(String username, String fromAccountNumber, String toAccountNumber, double amount) {
        if (amount <= 0) return false;
        if (fromAccountNumber.equals(toAccountNumber)) return false;
        Account from = findAccount(username, fromAccountNumber);
        Account to = findAccount(username, toAccountNumber);
        if (from == null || to == null) return false;
        boolean withdrawn = from.withdraw(amount);
        if (!withdrawn) return false;
        to.deposit(amount);
        return true;
    }

    // Persistence helpers
    public List<Customer> exportCustomers() {
        return new ArrayList<>(customers);
    }

    public void importCustomers(List<Customer> imported) {
        customers.clear();
        if (imported != null) customers.addAll(imported);
    }
}
