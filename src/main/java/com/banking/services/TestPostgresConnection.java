package com.banking.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestPostgresConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bankdb"; // your database name here
        String user = "postgres"; // your DB username
        String password = "admin"; // your DB password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully to PostgreSQL!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
