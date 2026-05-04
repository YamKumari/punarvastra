package com.punarvastra.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides JDBC connections to the Punarvastra MySQL schema (XAMPP default).
 */
public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/punarvastra?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection() {
    }

    /**
     * Opens a new connection from the pool-less driver manager.
     *
     * @return live JDBC connection (caller must close)
     * @throws SQLException if the database is unreachable or the JDBC driver is missing
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
