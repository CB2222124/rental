package com.github.cb2222124.rental.commands;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abstract command test class that allows children to work on a clean database copy for unit tests.
 * See create-tables.sql & populate-tables.sql for database schema used when testing.
 *
 * @author Callan
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CommandTest {

    private static final String TEST_URL = "jdbc:postgresql://localhost:5432/rental";
    protected Connection connection;

    @BeforeAll
    public void connectDatabase() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "postgres");
        connection = DriverManager.getConnection(TEST_URL, properties);
    }

    @BeforeEach
    public void resetDatabase() throws SQLException {
        CallableStatement create = connection.prepareCall("{call createTables()}");
        CallableStatement populate = connection.prepareCall("{call populateTables()}");
        create.execute();
        populate.execute();
    }

    @AfterAll
    public void disconnectDatabase() throws SQLException {
        connection.close();
    }
}
