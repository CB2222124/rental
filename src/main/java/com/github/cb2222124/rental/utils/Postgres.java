package com.github.cb2222124.rental.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Postgres {

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
}



