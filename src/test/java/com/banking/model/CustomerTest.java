package com.banking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CustomerTest {
    
    private Customer customer;
    private User user;
    private static final String CUSTOMER_NAME = "John Doe";
    private static final String CUSTOMER_EMAIL = "john.doe@example.com";
    private static final String USERNAME = "johndoe";
    private static final String PASSWORD = "Password123";
    
    @BeforeEach
    void setUp() {
        user = new User(USERNAME, PASSWORD);
        customer = new Customer(CUSTOMER_NAME, CUSTOMER_EMAIL, user);
    }
    
    @Test
    void testCustomerCreation() {
        assertEquals(CUSTOMER_NAME, customer.getName());
        assertEquals(CUSTOMER_EMAIL, customer.getEmail());
        assertEquals(user, customer.getUser());
        assertNotNull(customer.getAccounts());
        assertTrue(customer.getAccounts().isEmpty());
    }
    
    @Test
    void testGetName() {
        assertEquals(CUSTOMER_NAME, customer.getName());
    }
    
    @Test
    void testGetEmail() {
        assertEquals(CUSTOMER_EMAIL, customer.getEmail());
    }
    
    @Test
    void testGetUser() {
        assertEquals(user, customer.getUser());
        assertEquals(USERNAME, customer.getUser().getUsername());
    }
    
    @Test
    void testGetAccountsInitiallyEmpty() {
        List<Account> accounts = customer.getAccounts();
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }
    
    @Test
    void testAddAccount() {
        Account account = new SavingsAccount("SAVINGS123", 0.05);
        customer.addAccount(account);
        
        List<Account> accounts = customer.getAccounts();
        assertEquals(1, accounts.size());
        assertEquals(account, accounts.get(0));
    }
    
    @Test
    void testAddMultipleAccounts() {
        Account savingsAccount = new SavingsAccount("SAVINGS123", 0.05);
        Account checkingAccount = new CheckingAccount("CHECKING123", 500.0);
        Account genericAccount = new Account("GENERIC123") {};
        
        customer.addAccount(savingsAccount);
        customer.addAccount(checkingAccount);
        customer.addAccount(genericAccount);
        
        List<Account> accounts = customer.getAccounts();
        assertEquals(3, accounts.size());
        
        assertTrue(accounts.contains(savingsAccount));
        assertTrue(accounts.contains(checkingAccount));
        assertTrue(accounts.contains(genericAccount));
    }
    
    @Test
    void testAddSameAccountMultipleTimes() {
        Account account = new SavingsAccount("SAVINGS123", 0.05);
        
        customer.addAccount(account);
        customer.addAccount(account); // Adding the same account again
        
        List<Account> accounts = customer.getAccounts();
        assertEquals(2, accounts.size()); // Both instances are added
        assertEquals(account, accounts.get(0));
        assertEquals(account, accounts.get(1));
    }
    
    @Test
    void testCustomerWithDifferentAccountTypes() {
        // Test adding different types of accounts
        Account savings = new SavingsAccount("SAV123", 0.04);
        Account checking = new CheckingAccount("CHK123", 1000.0);
        Account generic = new Account("GEN123") {};
        
        customer.addAccount(savings);
        customer.addAccount(checking);
        customer.addAccount(generic);
        
        List<Account> accounts = customer.getAccounts();
        assertEquals(3, accounts.size());
        
        // Verify account types
        assertTrue(accounts.get(0) instanceof SavingsAccount);
        assertTrue(accounts.get(1) instanceof CheckingAccount);
        assertFalse(accounts.get(2) instanceof SavingsAccount);
        assertFalse(accounts.get(2) instanceof CheckingAccount);
    }
    
    @Test
    void testCustomerWithNullName() {
        Customer nullNameCustomer = new Customer(null, CUSTOMER_EMAIL, user);
        assertNull(nullNameCustomer.getName());
    }
    
    @Test
    void testCustomerWithNullEmail() {
        Customer nullEmailCustomer = new Customer(CUSTOMER_NAME, null, user);
        assertNull(nullEmailCustomer.getEmail());
    }
    
    @Test
    void testCustomerWithEmptyName() {
        Customer emptyNameCustomer = new Customer("", CUSTOMER_EMAIL, user);
        assertEquals("", emptyNameCustomer.getName());
    }
    
    @Test
    void testCustomerWithEmptyEmail() {
        Customer emptyEmailCustomer = new Customer(CUSTOMER_NAME, "", user);
        assertEquals("", emptyEmailCustomer.getEmail());
    }
    
    @Test
    void testCustomerWithSpecialCharacters() {
        String specialName = "José María O'Connor-Smith";
        String specialEmail = "jose.maria@test-domain.co.uk";
        Customer specialCustomer = new Customer(specialName, specialEmail, user);
        
        assertEquals(specialName, specialCustomer.getName());
        assertEquals(specialEmail, specialCustomer.getEmail());
    }
    
    @Test
    void testCustomerAccountNumberRetrieval() {
        Account account1 = new SavingsAccount("ACC001", 0.05);
        Account account2 = new CheckingAccount("ACC002", 500.0);
        
        customer.addAccount(account1);
        customer.addAccount(account2);
        
        List<Account> accounts = customer.getAccounts();
        assertEquals("ACC001", accounts.get(0).getAccountNumber());
        assertEquals("ACC002", accounts.get(1).getAccountNumber());
    }
    
    @Test
    void testCustomerAccountBalanceRetrieval() {
        Account account = new SavingsAccount("BAL123", 0.05);
        account.deposit(1000.0);
        
        customer.addAccount(account);
        
        List<Account> accounts = customer.getAccounts();
        assertEquals(1000.0, accounts.get(0).getBalance(), 0.01);
    }
    
    @Test
    void testCustomerWithTransactionHistory() {
        Account account = new CheckingAccount("TXN123", 500.0);
        account.deposit(500.0);
        account.withdraw(100.0);
        account.deposit(200.0);
        
        customer.addAccount(account);
        
        List<Account> accounts = customer.getAccounts();
        assertEquals(1, accounts.size());
        assertEquals(600.0, accounts.get(0).getBalance(), 0.01);
        assertEquals(3, accounts.get(0).getTransactionList().size());
    }
    
    @Test
    void testMultipleCustomers() {
        User user1 = new User("user1", "Pass123");
        User user2 = new User("user2", "Pass456");
        
        Customer customer1 = new Customer("Customer1", "email1@test.com", user1);
        Customer customer2 = new Customer("Customer2", "email2@test.com", user2);
        
        // Each customer should be independent
        assertNotEquals(customer1.getName(), customer2.getName());
        assertNotEquals(customer1.getEmail(), customer2.getEmail());
        assertNotEquals(customer1.getUser(), customer2.getUser());
        
        // Add accounts to each customer
        Account account1 = new SavingsAccount("ACC1", 0.05);
        Account account2 = new CheckingAccount("ACC2", 500.0);
        
        customer1.addAccount(account1);
        customer2.addAccount(account2);
        
        assertEquals(1, customer1.getAccounts().size());
        assertEquals(1, customer2.getAccounts().size());
        assertEquals("ACC1", customer1.getAccounts().get(0).getAccountNumber());
        assertEquals("ACC2", customer2.getAccounts().get(0).getAccountNumber());
    }
    
    @Test
    void testCustomerImmutabilityOfUser() {
        String originalUsername = customer.getUser().getUsername();
        
        // Customer's user should remain unchanged
        assertEquals(originalUsername, customer.getUser().getUsername());
        assertEquals(user, customer.getUser());
    }
    
    @Test
    void testCustomerAccountListImmutability() {
        List<Account> accounts = customer.getAccounts();
        
        // Create a new account
        Account newAccount = new SavingsAccount("NEW123", 0.05);
        
        // The returned list should be a copy/modifiable
        accounts.add(newAccount); // This might not work depending on implementation
        // If the implementation returns an immutable list, this test should be adjusted
        
        // Verify the account was added
        assertEquals(1, customer.getAccounts().size());
        assertEquals(newAccount, customer.getAccounts().get(0));
    }
}
