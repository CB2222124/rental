package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * Command used to view all vehicles in the rental system.
 * Optional arguments: -employee Used to filter vehicle search to only those that have active bookings.
 *
 * @author Liam, Callan.
 */
public class ViewVehiclesCommandEmployee implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            if (args.containsKey("booked")) {
                showBookedVehicles(postgres.getConnection());
            } else {
                showAllVehicles(postgres.getConnection());
            }
        } catch (SQLException e) {
            System.out.print("Error connecting to database, search aborted.");
        }
    }

    /**
     * Shows all vehicles on database.
     *
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void showAllVehicles(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle");
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "reg", "make", "model", "location_id", "daily_fee"};
            String[] outputColumns = {"Vehicle ID", "Registration", "Make", "Model", "Location ID", "Daily Rate"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No vehicles found.");
        }
    }

    /**
     * Shows all vehicles on database with join to associated booking information (Only shows booked vehicles).
     *
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void showBookedVehicles(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select vehicle.vehicle_id, vehicle.reg, vehicle.make, vehicle.model, booking.customer_id, booking.datefrom, booking.dateto FROM vehicle INNER JOIN booking ON booking.vehicle_id = vehicle.vehicle_id ORDER BY vehicle.vehicle_id;");
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "reg", "make", "model", "customer_id", "datefrom", "dateto"};
            String[] outputColumns = {"Vehicle ID", "Registration", "Make", "Model", "Customer ID", "Collection Date", "Return Date"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No vehicles found.");
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "View all vehicles, add '-booked' to filter by booked only.";
    }
}
