package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Command used to register a customer account.
 *
 * @author Callan.
 */
public class RegisterCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            //Take user input.
            Scanner scanner = new Scanner(System.in);
            System.out.print("First Name: ");
            String firstname = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastname = scanner.nextLine();
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            //Basic sanity checks.
            if (username.isBlank() || password.isBlank()) {
                System.out.println("Username and password cannot be blank, customer registration aborted.");
            } else {
                addNewCustomer(firstname, lastname, username, password, postgres.getConnection());
                System.out.println("Customer account '" + username + "' created, please login.");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), customer registration aborted.");
        }
    }

    /**
     * Adds a new entry to customer table with provided arguments, provided a customer with the same
     * username does not already exist.
     *
     * @param firstname  Customer First Name.
     * @param lastname   Customer Last Name.
     * @param username   Customer Username.
     * @param password   Customer Password.
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors (Including UNIQUE constraint enforced on username column).
     */
    public void addNewCustomer(String firstname, String lastname, String username, String password,
                               Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO customer (firstname, lastname, username, password) VALUES (?,?,?,?)");
        statement.setString(1, firstname);
        statement.setString(2, lastname);
        statement.setString(3, username);
        statement.setString(4, password);
        statement.executeUpdate();
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Create a customer account with provided credentials.";
    }
}
