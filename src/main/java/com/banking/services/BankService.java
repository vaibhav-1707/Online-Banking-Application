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
}
