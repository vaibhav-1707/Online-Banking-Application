package com.banking.services;

import com.banking.model.*;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class DatabaseService {
    private String url;
    private String user;
    private String password;
    
    public DatabaseService() {
        try {
            Properties props = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input != null) {
                    props.load(input);
                    this.url = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/bankdb");
                    this.user = props.getProperty("db.username", "postgres");
                    this.password = props.getProperty("db.password", "admin");
                } else {
                    // Use defaults if config file not found
                    this.url = "jdbc:postgresql://localhost:5432/bankdb";
                    this.user = "postgres";
                    this.password = "admin";
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading database config, using defaults: " + e.getMessage());
            this.url = "jdbc:postgresql://localhost:5432/bankdb";
            this.user = "postgres";
            this.password = "admin";
        }
    }
    
    public DatabaseService(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    public void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            // Create tables if they don't exist
            createTables(conn);
        }
    }
    
    private void createTables(Connection conn) throws SQLException {
        // Users table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(50) PRIMARY KEY,
                password_hash VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        // Customers table
        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) REFERENCES users(username),
                email VARCHAR(100),
                name VARCHAR(100),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        // Accounts table
        String createAccountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(20) PRIMARY KEY,
                customer_id INTEGER REFERENCES customers(id),
                account_type VARCHAR(20) NOT NULL,
                balance DECIMAL(15,2) DEFAULT 0.00,
                interest_rate DECIMAL(5,4),
                overdraft_limit DECIMAL(15,2),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        // Transactions table
        String createTransactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id VARCHAR(36) PRIMARY KEY,
                account_number VARCHAR(20) REFERENCES accounts(account_number),
                transaction_type VARCHAR(20) NOT NULL,
                amount DECIMAL(15,2) NOT NULL,
                resulting_balance DECIMAL(15,2) NOT NULL,
                transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createCustomersTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
        }
    }
    
    // User operations - Note: User class doesn't expose password hash directly
    // We'll need to store a serialized version or modify the approach
    public void saveUser(User user) throws SQLException {
        // For now, we'll skip user saving since we can't access the password hash
        // This would need to be modified in the User class to expose a serialization method
        System.out.println("User saving not implemented - requires User class modification");
    }
    
    public User loadUser(String username) throws SQLException {
        String sql = "SELECT username, password_hash FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password_hash"));
                }
            }
        }
        return null;
    }
    
    public List<User> loadAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, password_hash FROM users";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("password_hash")));
            }
        }
        return users;
    }
    
    // Customer operations
    public void saveCustomer(Customer customer) throws SQLException {
        // First save the user
        saveUser(customer.getUser());
        
        String sql = "INSERT INTO customers (username, email, name) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (username) DO UPDATE SET " +
                    "email = EXCLUDED.email, name = EXCLUDED.name";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getUser().getUsername());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getName());
            pstmt.executeUpdate();
        }
        
        // Save accounts
        for (Account account : customer.getAccounts()) {
            saveAccount(account, customer.getUser().getUsername());
        }
    }
    
    public Customer loadCustomer(String username) throws SQLException {
        String sql = "SELECT c.*, u.username, u.password_hash FROM customers c " +
                    "JOIN users u ON c.username = u.username WHERE c.username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Note: We can't reconstruct User without password, so we'll create a dummy one
                    // This is a limitation of the current User class design
                    User user = new User(rs.getString("username"), "dummy_password");
                    Customer customer = new Customer(
                        rs.getString("name") != null ? rs.getString("name") : username,
                        rs.getString("email"),
                        user
                    );
                    
                    // Load accounts for this customer
                    List<Account> accounts = loadAccountsForCustomer(username);
                    for (Account account : accounts) {
                        customer.addAccount(account);
                    }
                    
                    return customer;
                }
            }
        }
        return null;
    }
    
    public List<Customer> loadAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT DISTINCT username FROM customers";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String username = rs.getString("username");
                Customer customer = loadCustomer(username);
                if (customer != null) {
                    customers.add(customer);
                }
            }
        }
        return customers;
    }
    
    // Account operations
    private void saveAccount(Account account, String username) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, " +
                    "interest_rate, overdraft_limit) VALUES (?, " +
                    "(SELECT id FROM customers WHERE username = ?), ?, ?, ?, ?) " +
                    "ON CONFLICT (account_number) DO UPDATE SET " +
                    "balance = EXCLUDED.balance, interest_rate = EXCLUDED.interest_rate, " +
                    "overdraft_limit = EXCLUDED.overdraft_limit";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, username);
            pstmt.setString(3, getAccountType(account));
            pstmt.setBigDecimal(4, java.math.BigDecimal.valueOf(account.getBalance()));
            
            if (account instanceof SavingsAccount) {
                pstmt.setBigDecimal(5, java.math.BigDecimal.valueOf(((SavingsAccount) account).getInterestRate()));
                pstmt.setNull(6, Types.DECIMAL);
            } else if (account instanceof CheckingAccount) {
                pstmt.setNull(5, Types.DECIMAL);
                pstmt.setBigDecimal(6, java.math.BigDecimal.valueOf(((CheckingAccount) account).getOverdraftLimit()));
            } else {
                pstmt.setNull(5, Types.DECIMAL);
                pstmt.setNull(6, Types.DECIMAL);
            }
            
            pstmt.executeUpdate();
        }
        
        // Save transactions
        for (Transaction transaction : account.getTransactionList()) {
            saveTransaction(transaction, account.getAccountNumber());
        }
    }
    
    private List<Account> loadAccountsForCustomer(String username) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = (SELECT id FROM customers WHERE username = ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String accountNumber = rs.getString("account_number");
                    String accountType = rs.getString("account_type");
                    
                    Account account;
                    if ("SavingsAccount".equals(accountType)) {
                        double interestRate = rs.getBigDecimal("interest_rate").doubleValue();
                        account = new SavingsAccount(accountNumber, interestRate);
                    } else if ("CheckingAccount".equals(accountType)) {
                        double overdraftLimit = rs.getBigDecimal("overdraft_limit").doubleValue();
                        account = new CheckingAccount(accountNumber, overdraftLimit);
                    } else {
                        account = new Account(accountNumber) {};
                    }
                    
                    // Note: Account class doesn't have setBalance or addTransaction methods
                    // We'll need to modify the Account class or use reflection
                    // For now, we'll skip this functionality
                    System.out.println("Account balance and transaction loading not implemented - requires Account class modification");
                    
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }
    
    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) return "SavingsAccount";
        if (account instanceof CheckingAccount) return "CheckingAccount";
        return "Account";
    }
    
    // Transaction operations
    private void saveTransaction(Transaction transaction, String accountNumber) throws SQLException {
        String sql = "INSERT INTO transactions (transaction_id, account_number, transaction_type, " +
                    "amount, resulting_balance, transaction_date) VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (transaction_id) DO NOTHING";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getTransactionId());
            pstmt.setString(2, accountNumber);
            pstmt.setString(3, transaction.getType());
            pstmt.setBigDecimal(4, java.math.BigDecimal.valueOf(transaction.getAmount()));
            pstmt.setBigDecimal(5, java.math.BigDecimal.valueOf(transaction.getResultingBalance()));
            pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(transaction.getDate()));
            pstmt.executeUpdate();
        }
    }
    

    
    // Test connection
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
}
