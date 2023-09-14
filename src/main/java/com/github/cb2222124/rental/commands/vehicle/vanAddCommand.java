package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class vanAddCommand implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Registration: ");
            String reg = scanner.nextLine();
            System.out.print("Enter Make: ");
            String make = scanner.nextLine();
            System.out.print("Enter Model: ");
            String model = scanner.nextLine();
            System.out.print("Enter Location ID: ");
            int locationID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Daily Fee: ");
            double dailyFee = scanner.nextDouble();
            addNewVan(reg, make, model, locationID, dailyFee, postgres.getConnection());
            System.out.println("Van added successfully.");
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), add van operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Input type mismatch, add van operation aborted.");
        }
    }

    /**
     * Adds a new van with the specified information.
     *
     * @param reg        Van registration.
     * @param make       Van make.
     * @param model      Van model.
     * @param locationID Location.
     * @param dailyFee   Van daily fee.
     * @param connection The Postgres connection to execute the command on.
     * @throws SQLException Database errors.
     */
    public void addNewVan(String reg, String make, String model, int locationID, double dailyFee,
                          Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO van (reg, make, model, location_id, daily_fee) VALUES (?,?,?,?,?)");
        statement.setString(1, reg);
        statement.setString(2, make);
        statement.setString(3, model);
        statement.setInt(4, locationID);
        statement.setDouble(5, dailyFee);
        statement.executeUpdate();
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Add a new van.";
    }
}
