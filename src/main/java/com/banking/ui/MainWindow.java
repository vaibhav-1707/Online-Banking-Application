 package com.banking.ui;
 
 import com.banking.model.Account;
 import com.banking.model.CheckingAccount;
 import com.banking.model.Customer;
 import com.banking.model.SavingsAccount;
 import com.banking.model.Transaction;
 import com.banking.model.User;
 import com.banking.services.AuthenticationService;
 import com.banking.services.EmailService;
 import com.banking.services.BankService;
 
 import javax.swing.BorderFactory;
 import javax.swing.DefaultListModel;
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.JOptionPane;
 import javax.swing.JPanel;
 import javax.swing.JPasswordField;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingWorker;
 import javax.swing.SwingUtilities;
 import javax.swing.JFileChooser;
 import java.awt.BorderLayout;
 import java.awt.CardLayout;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import java.text.NumberFormat;
 import java.util.Locale;
 import java.util.List;
 import java.nio.file.Path;
 import java.time.Instant;
 import java.time.Duration;
 import java.util.Properties;
 import java.io.InputStream;
 import java.io.IOException;
 import javax.mail.MessagingException;


public class MainWindow extends JFrame {

     private final AuthenticationService authenticationService;
     private final BankService bankService;
    private final EmailService emailService;
 
     // Auth state
     private String currentUsername = null;
 
     // Root with cards
     private final JPanel cards = new JPanel(new CardLayout());
     private final String CARD_AUTH = "auth";
     private final String CARD_DASH = "dash";
 
     // Auth UI
     private final JTextField usernameField = new JTextField(16);
     private final JPasswordField passwordField = new JPasswordField(16);
     private final JLabel statusLabel = new JLabel(" ");
 
     // Dashboard UI
     private final DefaultListModel<String> accountsListModel = new DefaultListModel<>();
     private final JList<String> accountsList = new JList<>(accountsListModel);
     private final JLabel dashStatus = new JLabel(" ");
    private final NumberFormat INR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

     // Persistence and session
     private final com.banking.services.PersistenceService persistenceService = new com.banking.services.PersistenceService();
     private Instant lastActivity = Instant.now();
     private final Duration sessionTimeout = Duration.ofMinutes(15);
 
     public MainWindow(AuthenticationService authenticationService, BankService bankService) {
         this.authenticationService = authenticationService;
         this.bankService = bankService;
        this.emailService = loadEmailServiceFromConfig();
 
         setTitle("Online Banking Application");
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setSize(640, 420);
        setLocationRelativeTo(null);

         cards.add(buildAuthPanel(), CARD_AUTH);
         cards.add(buildDashboardPanel(), CARD_DASH);
 
         setContentPane(cards);
         showAuth();
     }

    private EmailService loadEmailServiceFromConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find config.properties. Email notifications will be disabled.");
                return null; // Or a "no-op" email service
            }
            props.load(input);
            String host = props.getProperty("mail.smtp.host");
            String port = props.getProperty("mail.smtp.port");
            String user = props.getProperty("mail.smtp.user");
            String pass = props.getProperty("mail.smtp.password");
            return new EmailService(host, port, user, pass);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
 
     private JPanel buildAuthPanel() {
         JPanel root = new JPanel(new BorderLayout(12, 12));
         root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
         root.add(new JLabel("Welcome to the Online Banking Application!", JLabel.CENTER), BorderLayout.NORTH);
 
         JPanel form = new JPanel(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.insets = new Insets(6, 6, 6, 6);
         gbc.anchor = GridBagConstraints.WEST;
 
         gbc.gridx = 0; gbc.gridy = 0;
         form.add(new JLabel("Username:"), gbc);
         gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
         form.add(usernameField, gbc);
 
         gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
         form.add(new JLabel("Password:"), gbc);
         gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
         form.add(passwordField, gbc);
 
         JPanel buttons = new JPanel();
         JButton loginButton = new JButton("Login");
         JButton registerButton = new JButton("Register");
         buttons.add(loginButton);
         buttons.add(registerButton);
 
         gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
         form.add(buttons, gbc);
 
         gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
         form.add(statusLabel, gbc);
 
         loginButton.addActionListener(e -> doLogin());
         registerButton.addActionListener(e -> doRegister());
 
         root.add(form, BorderLayout.CENTER);
         return root;
     }
 
     private JPanel buildDashboardPanel() {
         JPanel root = new JPanel(new BorderLayout(8, 8));
         root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
         root.add(new JLabel("Your Accounts:"), BorderLayout.NORTH);
         accountsList.setVisibleRowCount(10);
         root.add(new JScrollPane(accountsList), BorderLayout.CENTER);
 
         JPanel actions = new JPanel();
         JButton createBtn = new JButton("Create Account");
         JButton depositBtn = new JButton("Deposit");
         JButton withdrawBtn = new JButton("Withdraw");
         JButton transferBtn = new JButton("Transfer");
         JButton detailsBtn = new JButton("Account Details");
         JButton historyBtn = new JButton("View History");
          JButton refreshBtn = new JButton("Refresh");
          JButton logoutBtn = new JButton("Logout");
          JButton saveBtn = new JButton("Save");
          JButton loadBtn = new JButton("Load");
          JButton exportBtn = new JButton("Export History");
 
         actions.add(createBtn);
         actions.add(depositBtn);
         actions.add(withdrawBtn);
         actions.add(transferBtn);
         actions.add(detailsBtn);
         actions.add(historyBtn);
          actions.add(refreshBtn);
          actions.add(saveBtn);
          actions.add(loadBtn);
          actions.add(exportBtn);
          actions.add(logoutBtn);
 
         createBtn.addActionListener(e -> actionCreateAccount());
         depositBtn.addActionListener(e -> actionDeposit());
         withdrawBtn.addActionListener(e -> actionWithdraw());
         transferBtn.addActionListener(e -> actionTransfer());
         detailsBtn.addActionListener(e -> actionViewAccountDetails());
         historyBtn.addActionListener(e -> actionViewHistory());
          refreshBtn.addActionListener(e -> refreshAccountsList());
          logoutBtn.addActionListener(e -> doLogout());
          saveBtn.addActionListener(e -> actionSave());
          loadBtn.addActionListener(e -> actionLoad());
          exportBtn.addActionListener(e -> actionExportHistory());
 
         JPanel south = new JPanel(new BorderLayout());
         south.add(actions, BorderLayout.CENTER);
         south.add(dashStatus, BorderLayout.SOUTH);
         root.add(south, BorderLayout.SOUTH);
 
         return root;
     }
 
     private void showAuth() {
         ((CardLayout) cards.getLayout()).show(cards, CARD_AUTH);
     }
 
     private void showDash() {
         ((CardLayout) cards.getLayout()).show(cards, CARD_DASH);
         refreshAccountsList();
     }
 
      private void doRegister() {
         String username = usernameField.getText().trim();
         String password = new String(passwordField.getPassword());
         if (username.isEmpty() || password.isEmpty()) {
             setStatus("Please enter username and password.");
             return;
         }

        String email = JOptionPane.showInputDialog(this, "Please enter your email address for notifications:");
        if (email == null || email.isBlank() || !email.contains("@")) {
            setStatus("A valid email is required for registration.");
            return;
        }

         boolean ok = authenticationService.registerUser(username, password);
         if (ok) {
             // Also create a corresponding Customer profile
             Customer existing = bankService.findCustomerByUsername(username);
             if (existing == null) {
                 User u = authenticationService.getUser(username);
                 if (u != null) {
                    // Assuming Customer constructor is (name, email, user)
                     bankService.addCustomer(new Customer(username, email, u));
                 }
             }
             setStatus("Registered. You can now log in.");
            sendEmailAsync(email, "Welcome to Online Banking",
                    "Hello " + username + ",\n\n" +
                            "Thank you for registering with the Online Banking Application. We're glad to have you!");
         } else {
             if (authenticationService.getUser(username) != null) {
                 setStatus("Username already exists.");
             } else {
                 setStatus("Password must be at least 8 chars and include uppercase, lowercase, and a digit.");
             }
         }
     }
 
     private void doLogin() {
         String username = usernameField.getText().trim();
         String password = new String(passwordField.getPassword());
         if (username.isEmpty() || password.isEmpty()) {
             setStatus("Please enter username and password.");
             return;
         }
        User user = authenticationService.login(username, password);
        if (user != null) {
             currentUsername = username;
            Customer customer = bankService.findCustomerByUsername(username);
             if (customer == null) {
                 bankService.addCustomer(new Customer(username, "", user));
                customer = bankService.findCustomerByUsername(username);
             }
            showDash();
            touchActivity();

            if (customer != null && customer.getEmail() != null && !customer.getEmail().isBlank()) {
                sendEmailAsync(customer.getEmail(), "Security Alert: New Login", "Hello " + username + ",\n\nYour account was just accessed from our application. If this was not you, please contact support immediately.");
            }
         } else {
             setStatus("Invalid credentials.");
         }
     }
 
     private void doLogout() {
         currentUsername = null;
         usernameField.setText("");
         passwordField.setText("");
         setStatus(" ");
         dashStatus.setText(" ");
         showAuth();
     }
 
    private void refreshAccountsList() {
         accountsListModel.clear();
         if (currentUsername == null) return;
        if (isSessionExpired()) { doLogout(); return; }
         List<Account> accounts = bankService.getAccountsForCustomer(currentUsername);
        for (Account acc : accounts) {
            accountsListModel.addElement(acc.getAccountNumber() + " | Balance: " + INR.format(acc.getBalance()));
        }
        touchActivity();
     }
 
     private Customer getCurrentCustomer() {
         return currentUsername == null ? null : bankService.findCustomerByUsername(currentUsername);
     }
 
     private Account findAccountByNumber(String accountNumber) {
         Customer c = getCurrentCustomer();
         if (c == null) return null;
         for (Account a : c.getAccounts()) {
             if (a.getAccountNumber().equals(accountNumber)) return a;
         }
         return null;
     }
 
     private void actionCreateAccount() {
         String[] options = {"Savings", "Checking"};
         int choice = JOptionPane.showOptionDialog(this, "Choose account type:", "Create Account",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
         if (choice == JOptionPane.CLOSED_OPTION) return;
 
         String accountNumber = JOptionPane.showInputDialog(this, "Enter new account number:");
         if (accountNumber == null || accountNumber.isBlank()) return;
 
         Customer c = getCurrentCustomer();
         if (c == null) { dashStatus.setText("No customer context."); return; }

         // Prevent duplicate account numbers for the same customer
         if (findAccountByNumber(accountNumber) != null) {
             JOptionPane.showMessageDialog(this, "Account number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }
 
         try {
             if (choice == 0) { // Savings
                 String rateStr = JOptionPane.showInputDialog(this, "Enter interest rate (e.g., 0.05):");
                 if (rateStr == null) return;
                 double rate = Double.parseDouble(rateStr);
                 c.addAccount(new SavingsAccount(accountNumber, rate));
             } else { // Checking
                 String limitStr = JOptionPane.showInputDialog(this, "Enter overdraft limit (e.g., 500):");
                 if (limitStr == null) return;
                 double limit = Double.parseDouble(limitStr);
                 c.addAccount(new CheckingAccount(accountNumber, limit));
             }
             dashStatus.setText("Account created.");
             refreshAccountsList();
         } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
 
    private void actionDeposit() {
        String accountNumber = getSelectedAccountNumber();
        if (accountNumber == null) accountNumber = promptAccountNumber();
         if (accountNumber == null) return;
         Account acc = findAccountByNumber(accountNumber);
         if (acc == null) { dashStatus.setText("Account not found."); return; }
         try {
             String amountStr = JOptionPane.showInputDialog(this, "Amount to deposit:");
             if (amountStr == null) return;
             double amount = Double.parseDouble(amountStr);
             if (amount <= 0) {
                 JOptionPane.showMessageDialog(this, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
             }
             acc.deposit(amount);
             dashStatus.setText("Deposit successful.");
             refreshAccountsList();

            Customer customer = getCurrentCustomer();
            if (customer != null && customer.getEmail() != null && !customer.getEmail().isBlank()) {
                String subject = "Transaction Notification: Deposit";
                String body = String.format("Hello %s,\n\nA deposit of %s has been made to your account %s.\nYour new balance is %s.",
                        currentUsername, INR.format(amount), acc.getAccountNumber(), INR.format(acc.getBalance()));
                sendEmailAsync(customer.getEmail(), subject, body);
            }
         } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
 
    private void actionWithdraw() {
        String accountNumber = getSelectedAccountNumber();
        if (accountNumber == null) accountNumber = promptAccountNumber();
         if (accountNumber == null) return;
         Account acc = findAccountByNumber(accountNumber);
         if (acc == null) { dashStatus.setText("Account not found."); return; }
         try {
             String amountStr = JOptionPane.showInputDialog(this, "Amount to withdraw:");
             if (amountStr == null) return;
             double amount = Double.parseDouble(amountStr);
             if (amount <= 0) {
                 JOptionPane.showMessageDialog(this, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
             }
             boolean ok = acc.withdraw(amount);
             if (ok) {
                 dashStatus.setText("Withdrawal processed.");
                Customer customer = getCurrentCustomer();
                if (customer != null && customer.getEmail() != null && !customer.getEmail().isBlank()) {
                    String subject = "Transaction Notification: Withdrawal";
                    String body = String.format("Hello %s,\n\nA withdrawal of %s has been made from your account %s.\nYour new balance is %s.",
                            currentUsername, INR.format(amount), acc.getAccountNumber(), INR.format(acc.getBalance()));
                    sendEmailAsync(customer.getEmail(), subject, body);
                }
             } else {
                 JOptionPane.showMessageDialog(this, "Withdrawal failed. Check balance or limits.", "Error", JOptionPane.ERROR_MESSAGE);
             }
             refreshAccountsList();
         } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
 
      private void actionViewHistory() {
        if (currentUsername == null) { dashStatus.setText("Not logged in."); return; }
        if (isSessionExpired()) { doLogout(); return; }
        String accountNumber = getSelectedAccountNumber();
        if (accountNumber == null) accountNumber = promptAccountNumber();
         if (accountNumber == null) return;
         Account acc = findAccountByNumber(accountNumber);
         if (acc == null) { dashStatus.setText("Account not found."); return; }
         List<Transaction> txs = acc.getTransactionList();
         if (txs.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No transactions to show.");
             return;
         }
         StringBuilder sb = new StringBuilder();
         for (Transaction t : txs) {
             sb.append(t.getDescription()).append('\n');
         }
         JTextArea area = new JTextArea(sb.toString(), 15, 40);
         area.setEditable(false);
         JOptionPane.showMessageDialog(this, new JScrollPane(area), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
     }

      private void actionTransfer() {
          if (currentUsername == null) { dashStatus.setText("Not logged in."); return; }
          if (isSessionExpired()) { doLogout(); return; }
          List<Account> accounts = bankService.getAccountsForCustomer(currentUsername);
          if (accounts.size() < 2) {
              JOptionPane.showMessageDialog(this, "Need at least two accounts to transfer.");
              return;
          }
          String from = getSelectedAccountNumber();
          if (from == null) from = promptAccountNumber();
          if (from == null) return;
          String to = JOptionPane.showInputDialog(this, "Enter destination account number:");
          if (to == null || to.isBlank()) return;
          if (from.equals(to)) {
              JOptionPane.showMessageDialog(this, "Source and destination cannot be the same.");
              return;
          }
          String amountStr = JOptionPane.showInputDialog(this, "Amount to transfer:");
          if (amountStr == null) return;
          try {
              double amount = Double.parseDouble(amountStr);
              if (amount <= 0) {
                  JOptionPane.showMessageDialog(this, "Amount must be positive.");
                  return;
              }
              boolean ok = bankService.transfer(currentUsername, from, to, amount);
              if (ok) {
                  dashStatus.setText("Transfer complete.");
                  refreshAccountsList();

                 Customer customer = getCurrentCustomer();
                 Account fromAcc = findAccountByNumber(from);
                 if (customer != null && fromAcc != null && customer.getEmail() != null && !customer.getEmail().isBlank()) {
                     String subject = "Transaction Notification: Transfer Sent";
                     String body = String.format("Hello %s,\n\nA transfer of %s has been sent from your account %s to account %s.\nYour new balance for account %s is %s.",
                             currentUsername, INR.format(amount), from, to, from, INR.format(fromAcc.getBalance()));
                     sendEmailAsync(customer.getEmail(), subject, body);
                 }
                 touchActivity();
              } else {
                  JOptionPane.showMessageDialog(this, "Transfer failed. Check balances and accounts.", "Error", JOptionPane.ERROR_MESSAGE);
              }
          } catch (NumberFormatException ex) {
              JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
          }
      }

      private void actionViewAccountDetails() {
          if (currentUsername == null) { dashStatus.setText("Not logged in."); return; }
          if (isSessionExpired()) { doLogout(); return; }
          String accountNumber = getSelectedAccountNumber();
          if (accountNumber == null) accountNumber = promptAccountNumber();
          if (accountNumber == null) return;
          Account acc = findAccountByNumber(accountNumber);
          if (acc == null) { dashStatus.setText("Account not found."); return; }
          String type = (acc instanceof SavingsAccount) ? "Savings" : (acc instanceof CheckingAccount ? "Checking" : "Account");
          StringBuilder details = new StringBuilder();
          details.append("Account Number: ").append(acc.getAccountNumber()).append('\n');
          details.append("Type: ").append(type).append('\n');
          if (acc instanceof SavingsAccount sa) {
              details.append("Interest Rate: ").append(sa.getInterestRate()).append('\n');
          } else if (acc instanceof CheckingAccount ca) {
              details.append("Overdraft Limit: ").append(INR.format(ca.getOverdraftLimit())).append('\n');
          }
          if (acc.getCreatedAt() != null) {
              details.append("Created At: ").append(acc.getCreatedAt().toString()).append('\n');
          }
          details.append("Balance: ").append(INR.format(acc.getBalance())).append('\n');
          JOptionPane.showMessageDialog(this, details.toString(), "Account Details", JOptionPane.INFORMATION_MESSAGE);
      }

      private void actionSave() {
          JFileChooser chooser = new JFileChooser();
          chooser.setDialogTitle("Save Data");
          if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
              Path file = chooser.getSelectedFile().toPath();
              try {
                  persistenceService.save(file, authenticationService, bankService);
                  dashStatus.setText("Saved.");
              } catch (Exception ex) {
                  JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
          }
      }

      private void actionLoad() {
          JFileChooser chooser = new JFileChooser();
          chooser.setDialogTitle("Load Data");
          if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
              Path file = chooser.getSelectedFile().toPath();
              try {
                  persistenceService.load(file, authenticationService, bankService);
                  dashStatus.setText("Loaded.");
                  refreshAccountsList();
              } catch (Exception ex) {
                  JOptionPane.showMessageDialog(this, "Load failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
          }
      }

      private void actionExportHistory() {
          String accountNumber = getSelectedAccountNumber();
          if (accountNumber == null) accountNumber = promptAccountNumber();
          if (accountNumber == null) return;
          Account acc = findAccountByNumber(accountNumber);
          if (acc == null) { dashStatus.setText("Account not found."); return; }
          JFileChooser chooser = new JFileChooser();
          chooser.setDialogTitle("Export Transaction History");
          if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
              Path file = chooser.getSelectedFile().toPath();
              try (java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(file)) {
                  for (Transaction t : acc.getTransactionList()) {
                      writer.write(t.getDescription());
                      writer.newLine();
                  }
                  dashStatus.setText("Exported.");
              } catch (Exception ex) {
                  JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
          }
      }
   
 
     private String promptAccountNumber() {
         return JOptionPane.showInputDialog(this, "Enter account number:");
     }

    private String getSelectedAccountNumber() {
        String selected = accountsList.getSelectedValue();
        if (selected == null || selected.isBlank()) return null;
        int sep = selected.indexOf(" | ");
        if (sep <= 0) return null;
        return selected.substring(0, sep);
    }
 
     private void setStatus(String message) {
         statusLabel.setText(message);
     }
 
     private void sendEmailAsync(String to, String subject, String body) {
        if (emailService == null) return; // Do nothing if email service failed to load
         new SwingWorker<Void, Void>() {
             @Override
             protected Void doInBackground() throws MessagingException {
                 emailService.sendEmail(to, subject, body);
                 return null;
             }
 
             @Override
             protected void done() {
                 try {
                     get(); // This will re-throw the exception from doInBackground
                 } catch (Exception e) {
                    Throwable cause = e.getCause(); // The cause will be the MessagingException
                    String errorMessage = "Could not send notification email.\nReason: " + (cause != null ? cause.getMessage() : e.getMessage());
                    JOptionPane.showMessageDialog(MainWindow.this, errorMessage, "Email Error", JOptionPane.WARNING_MESSAGE);
                 }
             }
         }.execute();
     }

     // Standalone launcher (optional)
  private void touchActivity() { lastActivity = java.time.Instant.now(); }
  private boolean isSessionExpired() { return currentUsername != null && java.time.Instant.now().isAfter(lastActivity.plus(sessionTimeout)); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
             var auth = new com.banking.services.AuthenticationService();
             var bank = new com.banking.services.BankService();
             MainWindow window = new MainWindow(auth, bank);
             window.setVisible(true);
        });
    }
}
