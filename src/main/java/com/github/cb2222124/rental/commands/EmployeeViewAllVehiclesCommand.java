package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author Liam
 */
public class EmployeeViewAllVehiclesCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        try {
            Postgres postgres = new Postgres();
            boolean bookedOnly = args.containsKey("booked");
            if (bookedOnly) {
                showBookedVehicles(postgres.getConnection());

            } else {
                showAllVehicles(postgres.getConnection());
            }
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.print("Error connecting to database, search aborted.");
        }
    }

    public void showAllVehicles(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle");
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "reg", "make", "model", "available", "location_id", "daily_fee"};
            String[] outputColumns = {"Vehicle ID", "Registration", "Make", "Model", "Available", "Location ID", "Daily Rate"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No vehicles found.");
        }
    }

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
        return "View all vehicles or filter by booked only by adding -booked.";
    }
}
