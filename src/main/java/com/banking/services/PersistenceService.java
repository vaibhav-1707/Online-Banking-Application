package com.banking.services;

import com.banking.model.Customer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple binary serialization-based persistence for demo purposes.
 */
public class PersistenceService {

    public void save(Path file, AuthenticationService auth, BankService bank) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))) {
            out.writeObject(auth.exportUsers());
            out.writeObject(bank.exportCustomers());
        }
    }

    @SuppressWarnings("unchecked")
    public void load(Path file, AuthenticationService auth, BankService bank) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) return;
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            Object usersObj = in.readObject();
            Object customersObj = in.readObject();
            Map<String, com.banking.model.User> importedUsers =
                    (usersObj instanceof Map) ? (Map<String, com.banking.model.User>) usersObj : new HashMap<>();
            List<Customer> importedCustomers =
                    (customersObj instanceof List) ? (List<Customer>) customersObj : java.util.Collections.emptyList();
            auth.importUsers(importedUsers);
            bank.importCustomers(importedCustomers);
        }
    }
}


