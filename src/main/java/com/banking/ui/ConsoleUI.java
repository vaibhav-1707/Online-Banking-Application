package com.banking.ui;

import com.banking.model.*;
import com.banking.services.AuthenticationService;
import com.banking.services.BankService;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * ConsoleUI provides a simple text-based interface for users to interact with the bank system.
 */
public class ConsoleUI {

    private AuthenticationService authService;
    private BankService bankService;
    private Scanner scanner;
    private User loggedInUser;
    private final NumberFormat INR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

    public ConsoleUI(AuthenticationService authService, BankService bankService) {
        this.authService = authService;
        this.bankService = bankService;
        this.scanner = new Scanner(System.in);
        this.loggedInUser = null;
    }

    public void start() {
        System.out.println("Welcome to the Online Banking Application!");
        boolean running = true;
        while (running) {
            if (loggedInUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number.");
            return;
        }

        switch (option) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                System.out.println("Thank you for using the Online Banking Application. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    private void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = authService.registerUser(username, password);
        if (success) {
            System.out.println("Registration successful! You can now login.");
            // Create corresponding Customer object linked with this user
            if (bankService.findCustomerByUsername(username) == null) {
                Customer newCustomer = new Customer(username, "", new User(username, password));
                bankService.addCustomer(newCustomer);
            }
        } else {
            if (authService.getUser(username) != null) {
                System.out.println("Username already exists. Try a different one.");
            } else {
                System.out.println("Password must be at least 8 chars and include uppercase, lowercase, and a digit.");
            }
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Login successful! Welcome, " + username);
            if (bankService.findCustomerByUsername(username) == null) {
                bankService.addCustomer(new Customer(username, "", new User(username, password)));
            }
        } else {
            System.out.println("Invalid username or password. Try again.");
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. View Accounts");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Transaction History");
        System.out.println("5. Create New Account");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number.");
            return;
        }

        switch (option) {
            case 1:
                viewAccounts();
                break;
            case 2:
                deposit();
                break;
            case 3:
                withdraw();
                break;
            case 4:
                viewTransactionHistory();
                break;
            case 5:
                createAccount();
                break;
            case 6:
                loggedInUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    private void viewAccounts() {
        List<Account> accounts = bankService.getAccountsForCustomer(loggedInUser.getUsername());
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts.");
        } else {
            System.out.println("Your accounts:");
            for (Account account : accounts) {
                System.out.println("Account Number: " + account.getAccountNumber() + ", Balance: " + INR.format(account.getBalance()));
            }
        }
    }

    private void deposit() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account account = findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter amount to deposit: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        account.deposit(amount);
        System.out.println("Deposit successful. New balance: " + INR.format(account.getBalance()));
    }

    private void withdraw() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account account = findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter amount to withdraw: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        boolean ok = account.withdraw(amount);
        if (ok) {
            System.out.println("Withdrawal processed. New balance: " + INR.format(account.getBalance()));
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    private void viewTransactionHistory() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        Account account = findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        List<Transaction> transactions = account.getTransactionList();
        if (transactions.isEmpty()) {
            System.out.println("No transactions to show.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getDescription());
            }
        }
    }

    private void createAccount() {
        System.out.println("\nChoose account type to create:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
        System.out.print("Enter choice: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number.");
            return;
        }

        System.out.print("Enter new account number: ");
        String accountNumber = scanner.nextLine();
        if (accountNumber == null || accountNumber.isBlank()) {
            System.out.println("Account number cannot be empty.");
            return;
        }

        Customer customer = bankService.findCustomerByUsername(loggedInUser.getUsername());
        if (customer == null) {
            System.out.println("Error: Customer not found.");
            return;
        }

        // Prevent duplicate account numbers for the same customer
        if (findAccountByNumber(accountNumber) != null) {
            System.out.println("Account number already exists.");
            return;
        }

        switch (choice) {
            case 1:
                System.out.print("Enter interest rate (e.g., 0.05 for 5%): ");
                double interestRate;
                try {
                    interestRate = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid interest rate.");
                    return;
                }
                SavingsAccount savingsAccount = new SavingsAccount(accountNumber, interestRate);
                customer.addAccount(savingsAccount);
                System.out.println("Savings account created successfully.");
                break;
            case 2:
                System.out.print("Enter overdraft limit (e.g., 500): ");
                double overdraftLimit;
                try {
                    overdraftLimit = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid overdraft limit.");
                    return;
                }
                CheckingAccount checkingAccount = new CheckingAccount(accountNumber, overdraftLimit);
                customer.addAccount(checkingAccount);
                System.out.println("Checking account created successfully.");
                break;
            default:
                System.out.println("Invalid account type choice.");
        }
    }

    private Account findAccountByNumber(String accountNumber) {
        List<Account> accounts = bankService.getAccountsForCustomer(loggedInUser.getUsername());
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        AuthenticationService auth = new AuthenticationService();
        BankService bank = new BankService();
        ConsoleUI app = new ConsoleUI(auth, bank);
        app.start();
    }
}
