package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Command used to log in to the application.
 * TODO: Requires rewrite after database refactor.
 *
 * @author Callan.
 */
public class LoginCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            Postgres postgres = new Postgres();
            if (login("customer", username, password, postgres.getConnection())) {
                Application.role = Role.CUSTOMER;
                System.out.println("Customer login successful.");
            } else if (login("employee", username, password, postgres.getConnection())) {
                Application.role = Role.EMPLOYEE;
                System.out.println("Employee login successful.");
            } else {
                System.out.println("Invalid credentials, login aborted.");
            }
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Error connecting to database, login aborted.");
        }
    }

    private boolean login(String table, String username, String password, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM " + table + " WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        return result.next();
    }

    @Override
    public boolean isAvailable() {
        return Application.role == Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Log into the application with provided credentials.";
    }
}
