package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Scanner;

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
            scanner.nextInt();
            if (changeVehicle(bookingID, vehicleID, postgres.getConnection())) {
                System.out.println("Vehicle for booking changed successfully.");
            } else {
                System.out.println("""
                        Invalid operation, changing the vehicle for a booking requires the following:
                        Current vehicle must not be in the customers possession.
                        New vehicle must be at the same location and available.""");
            }
        } catch (SQLException e) {
            System.out.println("Error accessing database, operation aborted.");
        }
    }

    private boolean changeVehicle(int bookingID, int vehicleID, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call updateBookingVehicle(?,?)}");
        statement.setInt(1, bookingID);
        statement.setInt(2, vehicleID);
        return statement.executeUpdate() != 0;
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
