package com.banking;

import com.banking.model.*;
import com.banking.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class IntegrationTest {
    
    private AuthenticationService authService;
    private BankService bankService;
    
    @BeforeEach
    void setUp() {
        authService = new AuthenticationService();
        bankService = new BankService();
    }
    
    @Test
    void testCompleteUserRegistrationAndLogin() {
        // Test complete user registration and login flow
        String username = "integrationuser";
        String password = "IntegrationPass123";
        String email = "integration@test.com";
        String name = "Integration Test User";
        
        // 1. Register user
        boolean registrationResult = authService.registerUser(username, password);
        assertTrue(registrationResult);
        
        // 2. Login user
        User user = authService.login(username, password);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        
        // 3. Create customer profile
        Customer customer = new Customer(name, email, user);
        bankService.addCustomer(customer);
        
        // 4. Verify customer was added
        Customer foundCustomer = bankService.findCustomerByUsername(username);
        assertNotNull(foundCustomer);
        assertEquals(name, foundCustomer.getName());
        assertEquals(email, foundCustomer.getEmail());
        
        // 5. Add accounts to customer
        Account savingsAccount = new SavingsAccount("SAV001", 0.05);
        Account checkingAccount = new CheckingAccount("CHK001", 500.0);
        
        customer.addAccount(savingsAccount);
        customer.addAccount(checkingAccount);
        
        // 6. Verify accounts were added
        List<Account> accounts = bankService.getAccountsForCustomer(username);
        assertEquals(2, accounts.size());
        
        // 7. Perform transactions
        savingsAccount.deposit(1000.0);
        checkingAccount.deposit(500.0);
        
        assertEquals(1000.0, savingsAccount.getBalance(), 0.01);
        assertEquals(500.0, checkingAccount.getBalance(), 0.01);
        
        // 8. Transfer between accounts
        boolean transferResult = bankService.transfer(username, "SAV001", "CHK001", 200.0);
        assertTrue(transferResult);
        
        assertEquals(800.0, savingsAccount.getBalance(), 0.01);
        assertEquals(700.0, checkingAccount.getBalance(), 0.01);
    }
    
    @Test
    void testMultipleUsersAndCustomers() {
        // Test multiple users and customers interacting
        String[] usernames = {"user1", "user2", "user3"};
        String[] names = {"User One", "User Two", "User Three"};
        String[] emails = {"user1@test.com", "user2@test.com", "user3@test.com"};
        
        // Register all users
        for (int i = 0; i < usernames.length; i++) {
            String password = "Password" + (i + 1) + "23";
            
            // Register user
            boolean regResult = authService.registerUser(usernames[i], password);
            assertTrue(regResult);
            
            // Login and verify
            User user = authService.login(usernames[i], password);
            assertNotNull(user);
            
            // Create customer
            Customer customer = new Customer(names[i], emails[i], user);
            bankService.addCustomer(customer);
            
            // Add accounts
            customer.addAccount(new SavingsAccount("SAV" + (i + 1), 0.05));
            customer.addAccount(new CheckingAccount("CHK" + (i + 1), 500.0));
        }
        
        // Verify all customers exist
        for (int i = 0; i < usernames.length; i++) {
            Customer found = bankService.findCustomerByUsername(usernames[i]);
            assertNotNull(found);
            assertEquals(names[i], found.getName());
            assertEquals(2, found.getAccounts().size());
        }
    }
    
    @Test
    void testAccountTypesAndTransactions() {
        // Test different account types with various transactions
        String username = "accounttypesuser";
        String password = "AccountTypes123";
        
        // Register user
        authService.registerUser(username, password);
        User user = authService.login(username, password);
        
        // Create customer
        Customer customer = new Customer("Account Types User", "accounttypes@test.com", user);
        bankService.addCustomer(customer);
        
        // Create different account types
        SavingsAccount savings = new SavingsAccount("SAV001", 0.05);
        CheckingAccount checking = new CheckingAccount("CHK001", 500.0);
        Account generic = new Account("GEN001") {};
        
        customer.addAccount(savings);
        customer.addAccount(checking);
        customer.addAccount(generic);
        
        // Test savings account functionality
        savings.deposit(1000.0);
        assertEquals(1000.0, savings.getBalance(), 0.01);
        
        savings.applyInterest();
        assertEquals(1050.0, savings.getBalance(), 0.01);
        
        // Test checking account overdraft
        checking.deposit(100.0);
        assertEquals(100.0, checking.getBalance(), 0.01);
        
        boolean overdraftResult = checking.withdraw(400.0);
        assertTrue(overdraftResult);
        assertEquals(-300.0, checking.getBalance(), 0.01);
        
        // Test generic account
        generic.deposit(500.0);
        assertEquals(500.0, generic.getBalance(), 0.01);
        
        boolean withdrawResult = generic.withdraw(200.0);
        assertTrue(withdrawResult);
        assertEquals(300.0, generic.getBalance(), 0.01);
        
        // Test transaction history
        assertEquals(2, savings.getTransactionList().size()); // deposit + interest
        assertEquals(2, checking.getTransactionList().size()); // deposit + withdrawal
        assertEquals(2, generic.getTransactionList().size()); // deposit + withdrawal
    }
    
    @Test
    void testTransferScenarios() {
        // Test various transfer scenarios
        String username = "transferscenariosuser";
        String password = "TransferScenarios123";
        
        // Register user
        authService.registerUser(username, password);
        User user = authService.login(username, password);
        
        // Create customer
        Customer customer = new Customer("Transfer Scenarios User", "transfers@test.com", user);
        bankService.addCustomer(customer);
        
        // Create accounts
        Account account1 = new SavingsAccount("ACC1", 0.05);
        Account account2 = new CheckingAccount("ACC2", 500.0);
        Account account3 = new Account("ACC3") {};
        
        customer.addAccount(account1);
        customer.addAccount(account2);
        customer.addAccount(account3);
        
        // Add initial balances
        account1.deposit(1000.0);
        account2.deposit(500.0);
        account3.deposit(2000.0);
        
        // Test successful transfers
        assertTrue(bankService.transfer(username, "ACC1", "ACC2", 200.0));
        assertEquals(800.0, account1.getBalance(), 0.01);
        assertEquals(700.0, account2.getBalance(), 0.01);
        
        assertTrue(bankService.transfer(username, "ACC3", "ACC1", 500.0));
        assertEquals(1300.0, account1.getBalance(), 0.01);
        assertEquals(1500.0, account3.getBalance(), 0.01);
        
        // Test overdraft transfer
        assertTrue(bankService.transfer(username, "ACC2", "ACC3", 400.0)); // Within overdraft limit
        assertEquals(300.0, account2.getBalance(), 0.01);
        assertEquals(1900.0, account3.getBalance(), 0.01);
        
        // Test failed transfers
        assertFalse(bankService.transfer(username, "ACC2", "ACC3", 1000.0)); // Exceeds overdraft
        assertFalse(bankService.transfer(username, "ACC1", "ACC1", 100.0)); // Same account
        assertFalse(bankService.transfer(username, "ACC1", "ACC2", -100.0)); // Negative amount
        assertFalse(bankService.transfer(username, "ACC1", "ACC2", 0.0)); // Zero amount
    }
    
    @Test
    void testLoginAttemptTracking() {
        // Test login attempt tracking with customer creation
        String username = "logintrackuser";
        String password = "LoginTrack123";
        
        // Register user
        authService.registerUser(username, password);
        
        // Create customer
        User user = authService.login(username, password);
        Customer customer = new Customer("Login Track User", "logintrack@test.com", user);
        bankService.addCustomer(customer);
        
        // Make failed login attempts
        for (int i = 0; i < 3; i++) {
            assertNull(authService.login(username, "WrongPassword"));
        }
        
        // Successful login should still work
        User successfulUser = authService.login(username, password);
        assertNotNull(successfulUser);
        
        // Make more failed attempts (should reset counter)
        for (int i = 0; i < 3; i++) {
            assertNull(authService.login(username, "WrongPassword"));
        }
        
        // Should still be able to login successfully
        assertNotNull(authService.login(username, password));
    }
    
    @Test
    void testDataPersistence() {
        // Test data persistence across service instances
        String username = "persistenceuser";
        String password = "Persistence123";
        
        // First service instance
        AuthenticationService auth1 = new AuthenticationService();
        BankService bank1 = new BankService();
        
        // Register and create customer
        auth1.registerUser(username, password);
        User user = auth1.login(username, password);
        Customer customer = new Customer("Persistence User", "persistence@test.com", user);
        customer.addAccount(new SavingsAccount("SAV001", 0.05));
        bank1.addCustomer(customer);
        
        // Export data
        var users = auth1.exportUsers();
        var customers = bank1.exportCustomers();
        
        // Second service instance
        AuthenticationService auth2 = new AuthenticationService();
        BankService bank2 = new BankService();
        
        // Import data
        auth2.importUsers(users);
        bank2.importCustomers(customers);
        
        // Verify data was transferred correctly
        User importedUser = auth2.login(username, password);
        assertNotNull(importedUser);
        
        Customer importedCustomer = bank2.findCustomerByUsername(username);
        assertNotNull(importedCustomer);
        assertEquals("Persistence User", importedCustomer.getName());
        assertEquals(1, importedCustomer.getAccounts().size());
    }
    
    @Test
    void testErrorHandling() {
        // Test error handling in various scenarios
        String username = "errorhandlinguser";
        String password = "ErrorHandling123";
        
        // Test with weak password
        assertFalse(authService.registerUser(username, "weak"));
        
        // Test with valid password
        assertTrue(authService.registerUser(username, password));
        
        // Test duplicate registration
        assertFalse(authService.registerUser(username, password));
        
        // Test non-existent user login
        assertNull(authService.login("nonexistent", password));
        
        // Test wrong password login
        assertNull(authService.login(username, "WrongPassword"));
        
        // Test customer operations with non-existent user
        List<Account> accounts = bankService.getAccountsForCustomer("nonexistent");
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
        
        Account account = bankService.findAccount("nonexistent", "ANYACCOUNT");
        assertNull(account);
        
        boolean transferResult = bankService.transfer("nonexistent", "FROM", "TO", 100.0);
        assertFalse(transferResult);
    }
    
    @Test
    void testBoundaryConditions() {
        // Test boundary conditions and edge cases
        String username = "boundaryuser";
        String password = "Boundary123";
        
        // Register user
        authService.registerUser(username, password);
        User user = authService.login(username, password);
        
        // Create customer
        Customer customer = new Customer("Boundary User", "boundary@test.com", user);
        bankService.addCustomer(customer);
        
        // Test with zero amounts
        Account account = new SavingsAccount("BOUNDARY", 0.05);
        customer.addAccount(account);
        
        account.deposit(0.0);
        assertEquals(0.0, account.getBalance(), 0.01);
        assertEquals(0, account.getTransactionList().size());
        
        account.deposit(100.0);
        boolean withdrawResult = account.withdraw(0.0);
        assertFalse(withdrawResult);
        assertEquals(100.0, account.getBalance(), 0.01);
        
        // Test with negative amounts
        account.withdraw(-50.0);
        assertEquals(100.0, account.getBalance(), 0.01);
        
        account.deposit(-50.0);
        assertEquals(100.0, account.getBalance(), 0.01);
        
        // Test transfer boundaries
        assertFalse(bankService.transfer(username, "BOUNDARY", "BOUNDARY", 0.0));
        assertFalse(bankService.transfer(username, "BOUNDARY", "BOUNDARY", -100.0));
        
        // Test with very large amounts
        account.deposit(Double.MAX_VALUE / 2);
        assertTrue(account.getBalance() > 0);
    }
}
