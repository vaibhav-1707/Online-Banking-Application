package com.banking.services;

import java.util.Properties;
import java.io.IOException;

/**
 * Simple test program to verify email configuration and functionality
 */
public class EmailTest {
    
    public static void main(String[] args) {
        System.out.println("=== Email Service Test ===");
        
        // Test 1: Load configuration
        System.out.println("\n1. Testing configuration loading...");
        Properties props = new Properties();
        try {
            props.load(EmailTest.class.getClassLoader().getResourceAsStream("config.properties"));
            System.out.println("✓ Configuration file loaded successfully");
            
            String host = props.getProperty("mail.smtp.host");
            String port = props.getProperty("mail.smtp.port");
            String user = props.getProperty("mail.username");
            String pass = props.getProperty("mail.password");
            
            System.out.println("  Host: " + host);
            System.out.println("  Port: " + port);
            System.out.println("  Username: " + user);
            System.out.println("  Password configured: " + (pass != null && !pass.isBlank() ? "Yes" : "No"));
            
            if (pass != null && pass.equals("YOUR_GMAIL_APP_PASSWORD_HERE")) {
                System.out.println("  ⚠️  Please configure your Gmail app password!");
            }
            
        } catch (IOException e) {
            System.err.println("✗ Failed to load configuration: " + e.getMessage());
            return;
        }
        
        // Test 2: Create EmailService
        System.out.println("\n2. Testing EmailService creation...");
        try {
            String host = props.getProperty("mail.smtp.host");
            String port = props.getProperty("mail.smtp.port");
            String user = props.getProperty("mail.username");
            String pass = props.getProperty("mail.password");
            
            if (host == null || port == null || user == null || pass == null) {
                System.err.println("✗ Configuration incomplete");
                return;
            }
            
            EmailService emailService = new EmailService(host, port, user, pass);
            System.out.println("✓ EmailService created successfully");
            
            // Test 3: Try to send a test email
            System.out.println("\n3. Testing email sending...");
            System.out.println("  Sending test email to: " + user);
            
            emailService.sendEmail(user, "Test Email from Banking App", 
                "This is a test email to verify the email service is working correctly.\n\n" +
                "If you receive this, the email configuration is working!\n\n" +
                "Sent at: " + java.time.LocalDateTime.now());
                
            System.out.println("✓ Test email sent successfully!");
            
        } catch (Exception e) {
            System.err.println("✗ EmailService test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}
