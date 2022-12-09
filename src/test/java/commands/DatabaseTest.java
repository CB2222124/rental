package commands;

import com.github.cb2222124.rental.utils.Postgres;
import org.junit.jupiter.api.BeforeEach;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Abstract database class that allows children to work on a clean database copy for unit tests.
 *
 * @author Callan
 */
public abstract class DatabaseTest {
    @BeforeEach
    public void resetDatabase() throws SQLException {
        try (Postgres postgres = new Postgres();
             CallableStatement statement = postgres.getConnection().prepareCall("{call getLocationsWithAddresses()}")) {
            statement.executeQuery();
        }
    }
}
