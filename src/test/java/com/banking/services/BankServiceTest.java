package com.banking.services;

import com.banking.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BankServiceTest {
    
    private BankService bankService;
    
    @BeforeEach
    void setUp() {
        bankService = new BankService();
    }
    
    @Test
    void testAddCustomer() {
        User user = new User("testuser", "Password123");
        Customer customer = new Customer("Test Customer", "test@example.com", user);
        
        bankService.addCustomer(customer);
        
        Customer foundCustomer = bankService.findCustomerByUsername("testuser");
        assertNotNull(foundCustomer);
        assertEquals("Test Customer", foundCustomer.getName());
        assertEquals("test@example.com", foundCustomer.getEmail());
    }
    
    @Test
    void testFindCustomerByUsername() {
        User user = new User("finduser", "Password123");
        Customer customer = new Customer("Find Customer", "find@example.com", user);
        
        bankService.addCustomer(customer);
        
        Customer foundCustomer = bankService.findCustomerByUsername("finduser");
        assertNotNull(foundCustomer);
        assertEquals("Find Customer", foundCustomer.getName());
    }
    
    @Test
    void testFindCustomerByInvalidUsername() {
        Customer foundCustomer = bankService.findCustomerByUsername("invaliduser");
        assertNull(foundCustomer);
    }
    
    @Test
    void testGetAccountsForCustomer() {
        User user = new User("accountuser", "Password123");
        Customer customer = new Customer("Account Customer", "account@example.com", user);
        
        // Add some accounts to the customer
        customer.addAccount(new SavingsAccount("SAV001", 0.05));
        customer.addAccount(new CheckingAccount("CHK001", 500.0));
        
        bankService.addCustomer(customer);
        
        List<Account> accounts = bankService.getAccountsForCustomer("accountuser");
        assertEquals(2, accounts.size());
        assertEquals("SAV001", accounts.get(0).getAccountNumber());
        assertEquals("CHK001", accounts.get(1).getAccountNumber());
    }
    
    @Test
    void testGetAccountsForNonExistentCustomer() {
        List<Account> accounts = bankService.getAccountsForCustomer("nonexistent");
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }
    
    @Test
    void testFindAccount() {
        User user = new User("findaccuser", "Password123");
        Customer customer = new Customer("Find Account Customer", "findacc@example.com", user);
        
        Account savingsAccount = new SavingsAccount("SAV002", 0.05);
        Account checkingAccount = new CheckingAccount("CHK002", 500.0);
        
        customer.addAccount(savingsAccount);
        customer.addAccount(checkingAccount);
        
        bankService.addCustomer(customer);
        
        // Find existing accounts
        Account foundSavings = bankService.findAccount("findaccuser", "SAV002");
        Account foundChecking = bankService.findAccount("findaccuser", "CHK002");
        
        assertNotNull(foundSavings);
        assertNotNull(foundChecking);
        assertEquals("SAV002", foundSavings.getAccountNumber());
        assertEquals("CHK002", foundChecking.getAccountNumber());
    }
    
    @Test
    void testFindNonExistentAccount() {
        User user = new User("nonexistentaccuser", "Password123");
        Customer customer = new Customer("No Account Customer", "noacc@example.com", user);
        bankService.addCustomer(customer);
        
        Account foundAccount = bankService.findAccount("nonexistentaccuser", "NONEXISTENT");
        assertNull(foundAccount);
    }
    
    @Test
    void testFindAccountForNonExistentCustomer() {
        Account foundAccount = bankService.findAccount("nonexistentuser", "ANYACCOUNT");
        assertNull(foundAccount);
    }
    
    @Test
    void testSuccessfulTransfer() {
        User user = new User("transferuser", "Password123");
        Customer customer = new Customer("Transfer Customer", "transfer@example.com", user);
        
        Account fromAccount = new CheckingAccount("FROM", 500.0);
        Account toAccount = new SavingsAccount("TO", 0.05);
        
        // Add some money to the source account
        fromAccount.deposit(1000.0);
        
        customer.addAccount(fromAccount);
        customer.addAccount(toAccount);
        
        bankService.addCustomer(customer);
        
        // Perform transfer
        boolean transferResult = bankService.transfer("transferuser", "FROM", "TO", 500.0);
        
        assertTrue(transferResult);
        assertEquals(500.0, fromAccount.getBalance(), 0.01);
        assertEquals(500.0, toAccount.getBalance(), 0.01);
    }
    
    @Test
    void testTransferWithInsufficientBalance() {
        User user = new User("insufficientuser", "Password123");
        Customer customer = new Customer("Insufficient Customer", "insufficient@example.com", user);
        
        Account fromAccount = new CheckingAccount("FROM2", 500.0);
        Account toAccount = new SavingsAccount("TO2", 0.05);
        
        // Add limited money to source account
        fromAccount.deposit(200.0);
        
        customer.addAccount(fromAccount);
        customer.addAccount(toAccount);
        
        bankService.addCustomer(customer);
        
        // Try to transfer more than available
        boolean transferResult = bankService.transfer("insufficientuser", "FROM2", "TO2", 500.0);
        
        assertFalse(transferResult);
        assertEquals(200.0, fromAccount.getBalance(), 0.01);
        assertEquals(0.0, toAccount.getBalance(), 0.01);
    }
    
    @Test
    void testTransferWithOverdraft() {
        User user = new User("overdraftuser", "Password123");
        Customer customer = new Customer("Overdraft Customer", "overdraft@example.com", user);
        
        Account fromAccount = new CheckingAccount("FROM3", 500.0); // Overdraft limit 500
        Account toAccount = new SavingsAccount("TO3", 0.05);
        
        // Add some money to source account
        fromAccount.deposit(100.0);
        
        customer.addAccount(fromAccount);
        customer.addAccount(toAccount);
        
        bankService.addCustomer(customer);
        
        // Try to transfer amount that requires overdraft
        boolean transferResult = bankService.transfer("overdraftuser", "FROM3", "TO3", 400.0);
        
        assertTrue(transferResult); // Should succeed with overdraft
        assertEquals(-300.0, fromAccount.getBalance(), 0.01); // 100 - 400 = -300
        assertEquals(400.0, toAccount.getBalance(), 0.01);
    }
    
    @Test
    void testTransferExceedingOverdraftLimit() {
        User user = new User("exceeduser", "Password123");
        Customer customer = new Customer("Exceed Customer", "exceed@example.com", user);
        
        Account fromAccount = new CheckingAccount("FROM4", 500.0); // Overdraft limit 500
        Account toAccount = new SavingsAccount("TO4", 0.05);
        
        // Add some money to source account
        fromAccount.deposit(100.0);
        
        customer.addAccount(fromAccount);
        customer.addAccount(toAccount);
        
        bankService.addCustomer(customer);
        
        // Try to transfer amount that exceeds overdraft
        boolean transferResult = bankService.transfer("exceeduser", "FROM4", "TO4", 800.0);
        
        assertFalse(transferResult); // Should fail - exceeds overdraft
        assertEquals(100.0, fromAccount.getBalance(), 0.01); // Balance unchanged
        assertEquals(0.0, toAccount.getBalance(), 0.01); // No transfer
    }
    
    @Test
    void testTransferToSameAccount() {
        User user = new User("sameaccuser", "Password123");
        Customer customer = new Customer("Same Account Customer", "sameacc@example.com", user);
        
        Account account = new SavingsAccount("SAME", 0.05);
        account.deposit(1000.0);
        
        customer.addAccount(account);
        bankService.addCustomer(customer);
        
        boolean transferResult = bankService.transfer("sameaccuser", "SAME", "SAME", 500.0);
        
        assertFalse(transferResult);
        assertEquals(1000.0, account.getBalance(), 0.01); // Balance unchanged
    }
    
    @Test
    void testTransferWithInvalidAmount() {
        User user = new User("invalidamountuser", "Password123");
        Customer customer = new Customer("Invalid Amount Customer", "invalidamount@example.com", user);
        
        Account fromAccount = new CheckingAccount("FROM5", 500.0);
        Account toAccount = new SavingsAccount("TO5", 0.05);
        
        fromAccount.deposit(1000.0);
        
        customer.addAccount(fromAccount);
        customer.addAccount(toAccount);
        
        bankService.addCustomer(customer);
        
        // Zero amount
        boolean transferResult1 = bankService.transfer("invalidamountuser", "FROM5", "TO5", 0.0);
        assertFalse(transferResult1);
        
        // Negative amount
        boolean transferResult2 = bankService.transfer("invalidamountuser", "FROM5", "TO5", -100.0);
        assertFalse(transferResult2);
        
        // Balance should be unchanged
        assertEquals(1000.0, fromAccount.getBalance(), 0.01);
        assertEquals(0.0, toAccount.getBalance(), 0.01);
    }
    
    @Test
    void testTransferWithNonExistentAccounts() {
        User user = new User("nonexistentaccuser", "Password123");
        Customer customer = new Customer("No Account Customer", "noacc@example.com", user);
        bankService.addCustomer(customer);
        
        // Try to transfer between non-existent accounts
        boolean transferResult = bankService.transfer("nonexistentaccuser", "FROM", "TO", 100.0);
        assertFalse(transferResult);
    }
    
    @Test
    void testTransferWithInsufficientAccounts() {
        User user = new User("oneaccuser", "Password123");
        Customer customer = new Customer("One Account Customer", "oneacc@example.com", user);
        
        Account account = new SavingsAccount("ONE", 0.05);
        customer.addAccount(account);
        
        bankService.addCustomer(customer);
        
        // Try to transfer with only one account
        boolean transferResult = bankService.transfer("oneaccuser", "ONE", "TWO", 100.0);
        assertFalse(transferResult);
    }
    
    @Test
    void testExportAndImportCustomers() {
        User user1 = new User("exportuser1", "Password123");
        User user2 = new User("exportuser2", "Password456");
        
        Customer customer1 = new Customer("Export Customer 1", "export1@example.com", user1);
        Customer customer2 = new Customer("Export Customer 2", "export2@example.com", user2);
        
        bankService.addCustomer(customer1);
        bankService.addCustomer(customer2);
        
        // Export customers
        List<Customer> exportedCustomers = bankService.exportCustomers();
        assertEquals(2, exportedCustomers.size());
        
        // Create new bank service and import
        BankService newBankService = new BankService();
        newBankService.importCustomers(exportedCustomers);
        
        // Verify customers were imported
        Customer found1 = newBankService.findCustomerByUsername("exportuser1");
        Customer found2 = newBankService.findCustomerByUsername("exportuser2");
        
        assertNotNull(found1);
        assertNotNull(found2);
        assertEquals("Export Customer 1", found1.getName());
        assertEquals("Export Customer 2", found2.getName());
    }
    
    @Test
    void testImportCustomersWithNullList() {
        // Should handle null gracefully
        bankService.importCustomers(null);
        
        // Service should still be in a valid state
        User user = new User("newuser", "Password123");
        Customer customer = new Customer("New Customer", "new@example.com", user);
        bankService.addCustomer(customer);
        
        assertNotNull(bankService.findCustomerByUsername("newuser"));
    }
    
    @Test
    void testImportCustomersClearsExisting() {
        // Add initial customer
        User user1 = new User("olduser", "Password123");
        Customer customer1 = new Customer("Old Customer", "old@example.com", user1);
        bankService.addCustomer(customer1);
        
        // Create new customer list
        User user2 = new User("newuser", "Password456");
        Customer customer2 = new Customer("New Customer", "new@example.com", user2);
        
        List<Customer> newCustomers = List.of(customer2);
        
        // Import new customers
        bankService.importCustomers(newCustomers);
        
        // Old customer should be gone
        assertNull(bankService.findCustomerByUsername("olduser"));
        
        // New customer should be accessible
        assertNotNull(bankService.findCustomerByUsername("newuser"));
    }
    
    @Test
    void testMultipleCustomersWithSameUsername() {
        User user1 = new User("duplicateuser", "Password123");
        User user2 = new User("duplicateuser", "Password456"); // Same username
        
        Customer customer1 = new Customer("Customer 1", "customer1@example.com", user1);
        Customer customer2 = new Customer("Customer 2", "customer2@example.com", user2);
        
        bankService.addCustomer(customer1);
        bankService.addCustomer(customer2);
        
        // Should find the last added customer
        Customer found = bankService.findCustomerByUsername("duplicateuser");
        assertEquals("Customer 2", found.getName());
    }
}
