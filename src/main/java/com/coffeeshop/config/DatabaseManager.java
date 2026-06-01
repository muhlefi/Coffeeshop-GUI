package com.coffeeshop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            AppConfig.getDbUrl(),
            AppConfig.getDbUsername(),
            AppConfig.getDbPassword()
        );
    }

    public static void testConnection() throws SQLException {
        try (Connection ignored = getConnection()) {
            // Connection close otomatis oleh try-with-resources.
        }
    }
}
