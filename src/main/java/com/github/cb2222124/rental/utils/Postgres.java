package com.github.cb2222124.rental.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for connecting to a Postgres Database.
 * Implements AutoCloseable so that it can be used in try-with-resources statements.
 * NOTE: In a deployed application the URL and properties of this class should be stored externally.
 *
 * @author Callan.
 */
public class Postgres implements AutoCloseable {

    private static final String URL = "jdbc:postgresql://localhost:5432/rental";

    private final Connection connection;

    public Postgres() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "postgres");
        connection = DriverManager.getConnection(URL, properties);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}



