package com.banking.services;

import com.banking.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service to generate and send monthly statements to customers
 */
public class MonthlyStatementService {
    private final EmailService emailService;
    private final ScheduledExecutorService scheduler;
    
    public MonthlyStatementService(EmailService emailService) {
        this.emailService = emailService;
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduleMonthlyStatements();
    }
    
    /**
     * Schedule monthly statements to be sent on the 1st of every month at 9:00 AM
     */
    private void scheduleMonthlyStatements() {
        // Calculate delay until next 1st of month at 9:00 AM
        long initialDelay = calculateInitialDelay();
        
        // Schedule to run every month (approximately 30 days)
        scheduler.scheduleAtFixedRate(
            this::generateAndSendMonthlyStatements,
            initialDelay,
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.MILLISECONDS
        );
        
        System.out.println("Monthly statement service scheduled. Next run in " + 
                          (initialDelay / (1000 * 60 * 60)) + " hours");
    }
    
    /**
     * Calculate delay until next 1st of month at 9:00 AM
     */
    private long calculateInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withDayOfMonth(1).withHour(9).withMinute(0).withSecond(0);
        
        // If we've already passed the 1st of this month, move to next month
        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusMonths(1);
        }
        
        return java.time.Duration.between(now, nextRun).toMillis();
    }
    
    /**
     * Generate and send monthly statements for all customers
     */
    public void generateAndSendMonthlyStatements() {
        try {
            System.out.println("Generating monthly statements...");
            
            // Get all customers (this would need to be implemented in BankService)
            // For now, we'll use a placeholder approach
            List<Customer> customers = getCustomersForStatements();
            
            for (Customer customer : customers) {
                generateAndSendStatement(customer);
            }
            
            System.out.println("Monthly statements completed for " + customers.size() + " customers");
            
        } catch (Exception e) {
            System.err.println("Error generating monthly statements: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate and send statement for a specific customer
     */
    public void generateAndSendStatement(Customer customer) {
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            System.out.println("Skipping statement for customer " + customer.getUser().getUsername() + " - no email");
            return;
        }
        
        try {
            String statement = generateStatement(customer);
            String subject = "Monthly Statement - " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
            
            emailService.sendEmail(customer.getEmail(), subject, statement);
            System.out.println("Monthly statement sent to " + customer.getEmail());
            
        } catch (Exception e) {
            System.err.println("Error sending statement to " + customer.getEmail() + ": " + e.getMessage());
        }
    }
    
    /**
     * Generate monthly statement content for a customer
     */
    private String generateStatement(Customer customer) {
        StringBuilder statement = new StringBuilder();
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        
        statement.append("MONTHLY STATEMENT\n");
        statement.append("================\n\n");
        statement.append("Customer: ").append(customer.getName()).append("\n");
        statement.append("Username: ").append(customer.getUser().getUsername()).append("\n");
        statement.append("Statement Period: ").append(lastMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append("\n");
        statement.append("Generated: ").append(now.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append("\n\n");
        
        List<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            statement.append("No accounts found.\n");
        } else {
            statement.append("ACCOUNT SUMMARY\n");
            statement.append("===============\n\n");
            
            for (Account account : accounts) {
                statement.append("Account: ").append(account.getAccountNumber()).append("\n");
                statement.append("Type: ").append(getAccountType(account)).append("\n");
                statement.append("Balance: ").append(formatCurrency(account.getBalance())).append("\n");
                
                // Get transactions for last month
                List<Transaction> monthlyTransactions = getTransactionsForMonth(account, lastMonth);
                if (!monthlyTransactions.isEmpty()) {
                    statement.append("\nTransactions for ").append(lastMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append(":\n");
                    statement.append("----------------------------------------\n");
                    
                    for (Transaction transaction : monthlyTransactions) {
                        statement.append(transaction.getDate().format(DateTimeFormatter.ofPattern("dd/MM")));
                        statement.append(" - ").append(transaction.getType().toUpperCase());
                        statement.append(": ").append(formatCurrency(transaction.getAmount()));
                        statement.append(" | Balance: ").append(formatCurrency(transaction.getResultingBalance())).append("\n");
                    }
                } else {
                    statement.append("\nNo transactions for ").append(lastMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append("\n");
                }
                
                statement.append("\n");
            }
        }
        
        statement.append("Thank you for banking with us!\n");
        statement.append("If you have any questions, please contact our support team.");
        
        return statement.toString();
    }
    
    /**
     * Get account type as string
     */
    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) return "Savings Account";
        if (account instanceof CheckingAccount) return "Checking Account";
        return "General Account";
    }
    
    /**
     * Format currency amount
     */
    private String formatCurrency(double amount) {
        return String.format("₹%.2f", amount);
    }
    
    /**
     * Get transactions for a specific month
     */
    private List<Transaction> getTransactionsForMonth(Account account, LocalDate month) {
        return account.getTransactionList().stream()
            .filter(t -> {
                LocalDate transactionDate = t.getDate().toLocalDate();
                return transactionDate.getMonth() == month.getMonth() && 
                       transactionDate.getYear() == month.getYear();
            })
            .toList();
    }
    
    /**
     * Get customers for statement generation
     * This is a placeholder - would need proper implementation
     */
    private List<Customer> getCustomersForStatements() {
        // This would need to be implemented to get all customers from the system
        // For now, return empty list to avoid errors
        return List.of();
    }
    
    /**
     * Manually trigger statement generation (for testing)
     */
    public void generateStatementsNow() {
        generateAndSendMonthlyStatements();
    }
    
    /**
     * Shutdown the scheduler
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
