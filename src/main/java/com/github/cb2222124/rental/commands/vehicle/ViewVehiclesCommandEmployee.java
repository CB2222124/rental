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

/**Command used to view all vehicles in the rental system
 * Argument flag '-booked' used in combination to filter vehicle search by those that have active bookings
 * @author Liam
 */
public class ViewVehiclesCommandEmployee implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
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

    /**
     * Function outputting all vehicles on database associated information.
     * @param connection
     * @throws SQLException
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
     * Function shows all vehicles on database with join to associated booking information.
     * @param connection
     * @throws SQLException
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
