package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Command used to cancel and active booking as an Employee
 * @author Liam
 */
public class EmployeeCancelBookingsCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Postgres postgres = new Postgres();
            // accepts -cancellation argument
            boolean cancelBooking = args.containsKey("cancellation");
            if (cancelBooking) {
                // accepts Booking ID for cancellation
                System.out.println("Enter Booking ID for Cancellation: ");
                int bookingIDCancel = scanner.nextInt();
                confirmCancellation(bookingIDCancel, postgres.getConnection());
            } else {
                System.out.println("Try -cancellation command, to cancel a booking.");
            }
            postgres.getConnection().close();

        } catch (SQLException e) {
            System.out.print("Error connecting to database, action aborted.");
        }

    }

    public void confirmCancellation(int bookingID, Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Enter 'yes' to confirm cancellation of Booking ID %d: ", bookingID);
        String confirmation = scanner.nextLine();

        //confirms request with 'yes', then executes SQL statement
        if (confirmation.equalsIgnoreCase("yes")) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM booking WHERE booking_id = ?");
            statement.setInt(1, bookingID); //??
            statement.executeUpdate();
            System.out.print("Cancellation confirmed.");
        } else {
            System.out.println("Cancellation aborted.");
        }
    }


    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }


    @Override
    public String getDescription() { return "Cancel a booking."; }
}


