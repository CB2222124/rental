package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Command used to log in to the application.
 * Optional arguments: -employee Used to specify the attempted login is for employee access level.
 *
 * @author Callan.
 */
public class LoginCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        //Take user input.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        //Evaluate login type.
        boolean employeeLogin = args.containsKey("employee");

        //Attempt appropriate login type and handle results.
        try (Postgres postgres = new Postgres()) {
            if (employeeLogin) {
                try {
                    int id = login("employee", username, password, postgres.getConnection());
                    Application.user.updateUser(Role.EMPLOYEE, id);
                    System.out.println("Employee login successful.");
                } catch (NoSuchElementException e) {
                    System.out.println("Invalid employee credentials, login aborted.");
                }
            } else {
                try {
                    int id = login("customer", username, password, postgres.getConnection());
                    Application.user.updateUser(Role.CUSTOMER, id);
                    System.out.println("Customer login successful.");
                } catch (NoSuchElementException e) {
                    System.out.println("Invalid customer credentials, login aborted.");
                    System.out.println("Attempting to access an employee account? Try 'login -employee'.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database, login aborted.");
        }
    }

    /**
     * Returns the ID associated with the provided user information.
     *
     * @param table      The table to search for user (Customer or Employee).
     * @param username   Username.
     * @param password   Password.
     * @param connection The Postgres connection to execute command on.
     * @return The ID found.
     * @throws SQLException           Database errors.
     * @throws NoSuchElementException No results found with provided information.
     */
    public int login(String table, String username, String password, Connection connection)
            throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT " + table + "_id FROM " + table + " WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        if (!result.next()) throw new NoSuchElementException();
        return result.getInt(1);
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Log into the application with provided credentials.";
    }
}
