package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Command used to list bookings. Behavior differs based on user role.
 * Employee: Lists all active bookings.
 * Customer: Lists all their active bookings.
 *
 * @author Callan
 */
public class ViewBookingsCommand implements Command {
    @Override
    public void execute(HashMap<String, String> args) {
        try {
            Postgres postgres = new Postgres();
            PreparedStatement statement;
            if (Application.user.getRole() == Role.EMPLOYEE) {
                statement = postgres.getConnection().prepareStatement("SELECT * FROM booking");
            } else if (Application.user.getRole() == Role.CUSTOMER) {
                statement = postgres.getConnection().prepareStatement("SELECT * FROM booking WHERE customer_id = ?");
                statement.setInt(1, Application.user.getID());
            } else {
                //NOTE: We should never get really, just a sanity check.
                System.out.println("Command unavailable to current user level, search aborted.");
                return;
            }
            ResultSet result = statement.executeQuery();
            if (result.isBeforeFirst()) {
                String[] resultColumns = {"booking_id", "customer_id", "vehicle_id", "pickup_loc", "dropoff_loc", "datefrom", "dateto", "with_customer"};
                String[] outputColumns = {"Booking ID", "Customer ID", "Vehicle ID", "Pickup Location ID", "Drop off Location ID", "Collection Date", "Return Date", "Customer Collected"};
                new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
            } else {
                System.out.println("No active bookings for user.");
            }
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Error connecting to database, booking search aborted.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + ", booking search aborted.");
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() != Role.NONE;
    }

    @Override
    public String getDescription() {
        return "View active bookings.";
    }
}
