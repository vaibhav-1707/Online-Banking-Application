package com.banking;

import com.banking.services.AuthenticationService;
import com.banking.services.BankService;
import com.banking.ui.MainWindow;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthenticationService auth = new AuthenticationService();
            BankService bank = new BankService();
            MainWindow window = new MainWindow(auth, bank);
            window.setVisible(true);
        });
    }
}
