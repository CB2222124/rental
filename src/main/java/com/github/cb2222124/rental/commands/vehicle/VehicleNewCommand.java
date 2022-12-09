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

/**
 * Command to add a new vehicle.
 *
 * @author Rania.
 */
public class VehicleNewCommand implements Command {
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
            addNewVehicle(reg, make, model, locationID, dailyFee, postgres.getConnection());
            System.out.println("Vehicle added successfully.");
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), add vehicle operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Input type mismatch, add vehicle operation aborted.");
        }
    }

    /**
     * Adds a new vehicle with the specified information.
     *
     * @param reg        Vehicle registration.
     * @param make       Vehicle make.
     * @param model      Vehicle model.
     * @param locationID Location.
     * @param dailyFee   Vehicle daily fee.
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void addNewVehicle(String reg, String make, String model, int locationID, double dailyFee,
                              Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO vehicle (reg, make, model, location_id, daily_fee) VALUES (?,?,?,?,?)");
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
        return "Add a new vehicle.";
    }
}
