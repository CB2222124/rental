package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Rania
 */
public class VehicleCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {

        if (args.containsKey("add")) {
            try {
                Postgres postgres = new Postgres();
                Scanner scanner = new Scanner(System.in);

                askForInput("Registration Number");
                String reg = scanner.nextLine();
                askForInput("Vehicle Make");
                String make = scanner.nextLine();
                askForInput("Vehicle Model");
                String model = scanner.nextLine();
                askForInput("Location ID");
                int location_id = scanner.nextInt();
                askForInput("Daily Fee");
                double daily_fee = scanner.nextDouble();
                addNewVehicle(reg, make, model, true, location_id, daily_fee, postgres.getConnection());

                System.out.println("Vehicle added successfully.");
                postgres.getConnection().close();
            } catch (SQLException e) {
                System.out.println("Database error (" + e.getMessage() + "), vehicle operation aborted.");
            } catch (InputMismatchException e) {
                System.out.println("Input type mismatch, vehicle operation aborted.");
            }
        } else {
            System.out.println("No valid vehicle operation specified.");
        }
    }

    public static void addNewVehicle(String reg, String make, String model, boolean availability,
                                     int location, double dailyFee, Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO vehicle (reg, make, model, available, location_id, daily_fee) VALUES (?, ?,?,?,?,?)");

        statement.setString(1, reg);
        statement.setString(2, make);
        statement.setString(3, model);
        statement.setBoolean(4, availability);
        statement.setInt(5, location);
        statement.setDouble(6, dailyFee);
        statement.executeUpdate();

    }

    public void askForInput(String requestedInput) {
        System.out.print("Enter " + requestedInput + ":");
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Perform operations on a specific vehicle.";
    }
}

