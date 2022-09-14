package com.config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/hibernate";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static Connection connection;

    private JDBCConfig() {
    }

    @SneakyThrows
    public static Connection getConnection() {

        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
