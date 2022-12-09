package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * @author Rania.
 */
public class VehicleNewCommandEmployee implements Command {
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
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
