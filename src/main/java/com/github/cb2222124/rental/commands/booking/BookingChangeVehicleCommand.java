package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Command to change the vehicle for a booking.
 *
 * @author Callan.
 */
public class BookingChangeVehicleCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Booking ID: ");
            int bookingID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter New Vehicle ID: ");
            int vehicleID = scanner.nextInt();
            if (changeVehicle(bookingID, vehicleID, Application.user.getID(), postgres.getConnection())) {
                System.out.println("Vehicle for booking changed successfully.");
            } else {
                System.out.println("""
                        Invalid operation, changing the vehicle for a booking requires the following:
                        Specified booking ID must belong to the customer.
                        Current vehicle must not be in the customers possession.
                        New vehicle must be at the same location and available.""");
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database, operation aborted.");
        }
    }

    /**
     * Changes the vehicle for a booking provided:
     * The old vehicle is not in the customers' possession.
     * The new vehicle is available and at the same location.
     * The booking belongs to the active user.
     *
     * @param bookingID  The booking ID.
     * @param vehicleID  The new vehicle.
     * @param connection The Postgres connection to execute command on.
     * @return Operation success.
     * @throws SQLException Database errors.
     */
    boolean changeVehicle(int bookingID, int vehicleID, int customerID, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call updateBookingVehicle(?,?,?)}");
        statement.setInt(1, bookingID);
        statement.setInt(2, vehicleID);
        statement.setInt(3, customerID);
        statement.registerOutParameter(1, Types.INTEGER);
        statement.execute();
        return statement.getInt(1) != 0;
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.CUSTOMER;
    }

    @Override
    public String getDescription() {
        return "Change the vehicle for a specific booking.";
    }
}
