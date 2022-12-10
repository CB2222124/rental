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
 * Employee command to record customer returning the vehicle.
 *
 * @author Callan.
 */
public class BookingDropoffCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Booking ID: ");
            if (recordDropoff(scanner.nextInt(), postgres.getConnection())) {
                System.out.println("Customer drop off recorded, booking removed.");
            } else {
                System.out.println("Invalid booking ID, verify booking exists and that the vehicle is in customers possession.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database, operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, operation aborted.");
        }
    }

    /**
     * Removes the booking as it has been completed.
     * TODO: The vehicles location should be changed to where it was dropped off.
     *
     * @param bookingID  The booking to update.
     * @param connection The Postgres connection to execute command on.
     * @return Operation success (Fails if the customer doesn't have the vehicle).
     * @throws SQLException Database errors.
     */
    public boolean recordDropoff(int bookingID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM booking WHERE booking_id = ? AND with_customer = 'true'");
        statement.setInt(1, bookingID);
        return statement.executeUpdate() != 0;
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Record customer has dropped off vehicle for booking.";
    }
}
