package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.utils.Postgres;
import org.junit.jupiter.api.BeforeEach;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Abstract command test class that allows children to work on a clean database copy for unit tests.
 * See create-tables.sql & populate-tables.sql for database schema used when testing.
 *
 * @author Callan
 */
public abstract class CommandTest {
    @BeforeEach
    public void resetDatabase() throws SQLException {
        try (Postgres postgres = new Postgres();
             CallableStatement create = postgres.getConnection().prepareCall("{call createTables()}");
             CallableStatement populate = postgres.getConnection().prepareCall("{call populateTables()}")) {
            create.execute();
            populate.execute();
        }
    }
}
