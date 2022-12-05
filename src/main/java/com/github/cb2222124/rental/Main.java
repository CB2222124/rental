package com.github.cb2222124.rental;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/rental";

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "postgres");
        Connection connection = DriverManager.getConnection(URL, properties);
    }
}