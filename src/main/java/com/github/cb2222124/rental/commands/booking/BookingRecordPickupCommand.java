package com.github.cb2222124.rental.commands.booking;

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
 * Employee command for recording a booked vehicle has been collected by the customer.
 *
 * @author Callan.
 */
public class BookingRecordPickupCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Booking ID: ");
            if (recordPickup(scanner.nextInt(), postgres.getConnection())) {
                System.out.println("Booking updated, vehicle is now in customer possession.");
            } else {
                System.out.println("Specified booking is already in customer possession.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database, operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, operation aborted.");
        }
    }

    /**
     * Updates the booking record to signify vehicle is in customers possession.
     *
     * @param bookingID  The booking to update.
     * @param connection The Postgres connection to execute command on.
     * @return Operation success (Fails if the customer already has the vehicle).
     * @throws SQLException Database errors.
     */
    public boolean recordPickup(int bookingID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE booking SET with_customer = 'true' WHERE booking_id = ?");
        statement.setInt(1, bookingID);
        return statement.executeUpdate() != 0;
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Record customer has picked up vehicle for booking.";
    }
}
