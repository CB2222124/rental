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
 * @author Rania, Callan, Saeed
 */
public class VehicleCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {

        try {
            Postgres postgres = new Postgres();
            Scanner scanner = new Scanner(System.in);

            //TODO: Lets process the first sequential argument. Otherwise we are prioritising arbitrary arguments which isn't obvious to the user.
            if (args.containsKey("add")) {
                System.out.print("Enter Registration: ");
                String reg = scanner.nextLine();
                System.out.print("Enter Make: ");
                String make = scanner.nextLine();
                System.out.print("Enter Model: ");
                String model = scanner.nextLine();
                System.out.print("Enter Location ID: ");
                int locationID = scanner.nextInt();
                System.out.print("Enter Daily Fee: ");
                double dailyFee = scanner.nextDouble();
                addNewVehicle(reg, make, model, locationID, dailyFee, postgres.getConnection());
                System.out.println("Vehicle added successfully.");
            } else if (args.containsKey("delete")) {
                System.out.print("Enter Vehicle ID: ");
                int vehicle_id = scanner.nextInt();
                if (deleteVehicle(vehicle_id, postgres.getConnection())) {
                    System.out.println("Vehicle deleted successfully.");
                } else {
                    System.out.println("No vehicle deleted, verify ID and availability.");
                }
            } else {
                System.out.println("No valid vehicle operation specified.");
            }
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), vehicle operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Input type mismatch, vehicle operation aborted.");
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

    public boolean deleteVehicle(int id, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM vehicle WHERE vehicle_id = ?");
        statement.setInt(1, id);
        return statement.executeUpdate() != 0;
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

