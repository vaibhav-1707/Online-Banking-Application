package com.banking.services;

/**
 * Simple test class to verify database connectivity
 */
public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing Database Connection...");
        
        DatabaseService dbService = new DatabaseService();
        
        try {
            // Test connection
            if (dbService.testConnection()) {
                System.out.println("✅ Database connection successful!");
                
                // Initialize database (create tables)
                dbService.initializeDatabase();
                System.out.println("✅ Database initialized successfully!");
                
            } else {
                System.out.println("❌ Database connection failed!");
                System.out.println("Please check:");
                System.out.println("1. PostgreSQL is running on localhost:5432");
                System.out.println("2. Database 'bankdb' exists");
                System.out.println("3. Username 'postgres' and password 'admin' are correct");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
