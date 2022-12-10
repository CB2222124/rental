package com.github.cb2222124.rental.commands.login;

import com.github.cb2222124.rental.commands.CommandTest;
import com.github.cb2222124.rental.commands.LoginCommand;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test valid and invalid logins.
 *
 * @author Callan
 */
public class LoginTest extends CommandTest {

    @Test
    public void validLogin() throws SQLException {
        LoginCommand loginCommand = new LoginCommand();
        int id = loginCommand.login("customer", "user101", "letmein", connection);
        assertEquals(1, id);
    }

    @Test
    public void throwOnInvalidLogin() {
        assertThrows(NoSuchElementException.class, () -> {
            LoginCommand loginCommand = new LoginCommand();
            loginCommand.login("customer", "user101", "letmein2", connection);
        });
    }

    @Test
    public void validLoginEmployee() throws SQLException {
        LoginCommand loginCommand = new LoginCommand();
        int id = loginCommand.login("employee", "admin", "password", connection);
        assertEquals(1, id);
    }

    @Test
    public void throwOnInvalidLoginEmployee() {
        assertThrows(NoSuchElementException.class, () -> {
            LoginCommand loginCommand = new LoginCommand();
            loginCommand.login("employee", "admin", "password2", connection);
        });
    }
}
