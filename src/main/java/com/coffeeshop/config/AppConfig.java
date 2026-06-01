package com.coffeeshop.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPERTIES.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Gagal membaca db.properties", e);
        }
    }

    private AppConfig() {
    }

    public static String getDbUrl() {
        String fromEnv = System.getenv("DB_URL");
        return fromEnv != null && !fromEnv.isBlank() ? fromEnv : PROPERTIES.getProperty("db.url");
    }

    public static String getDbUsername() {
        String fromEnv = System.getenv("DB_USERNAME");
        return fromEnv != null && !fromEnv.isBlank() ? fromEnv : PROPERTIES.getProperty("db.username");
    }

    public static String getDbPassword() {
        String fromEnv = System.getenv("DB_PASSWORD");
        return fromEnv != null ? fromEnv : PROPERTIES.getProperty("db.password");
    }
}
