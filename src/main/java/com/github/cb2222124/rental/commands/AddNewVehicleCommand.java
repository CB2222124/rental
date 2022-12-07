package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Rania
 */
public class AddNewVehicleCommand implements Command {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute(HashMap<String, String> args) {

        if (args.containsKey("addVehicle")) {
            try {
                Postgres postgres = new Postgres();

                askForInput("Registration Number");
                String reg = scanner.nextLine();

                askForInput("Vehicle Make");
                String make = scanner.nextLine();

                askForInput("Vehicle Model");
                String model = scanner.nextLine();

                askForInput("Availability enter 'true' or 'false'");
                boolean available = Boolean.parseBoolean(scanner.nextLine());


                askForInput("Location ID");
                int location_id = scanner.nextInt();

                askForInput("Daily Fee");
                double daily_fee = scanner.nextDouble();

                addNewVehicle(reg, make, model, available, location_id, daily_fee, postgres.getConnection());

                System.out.println("Vehicle added successfully");
                postgres.getConnection().close();
            } catch (SQLException e) {
                System.out.println("Error vehicle not added");
            }
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

    public static void askForInput(String requestedInput) {
        System.out.println("Enter " + requestedInput);

    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Add new vehicle ";
    }
}

