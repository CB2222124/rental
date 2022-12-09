package commands.login;

import com.github.cb2222124.rental.commands.LoginCommand;
import com.github.cb2222124.rental.utils.Postgres;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest {

    @BeforeEach
    public void resetDatabase() throws SQLException {
        try (Postgres postgres = new Postgres();
             CallableStatement statement = postgres.getConnection().prepareCall("{call getLocationsWithAddresses()}")) {
            statement.executeQuery();
        }
    }

    @Test
    public void validLogin() throws SQLException {
        try (Postgres postgres = new Postgres()) {
            LoginCommand loginCommand = new LoginCommand();
            int id = loginCommand.login("customer", "user101", "letmein", postgres.getConnection());
            assertEquals(1, id);
        }
    }

    @Test
    public void throwOnInvalidLogin() throws SQLException {
        try (Postgres postgres = new Postgres()) {
            assertThrows(NoSuchElementException.class, () -> {
                LoginCommand loginCommand = new LoginCommand();
                loginCommand.login("customer", "user101", "letmein2", postgres.getConnection());
            });
        }
    }
}
