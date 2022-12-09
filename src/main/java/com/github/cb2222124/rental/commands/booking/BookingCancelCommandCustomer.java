package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Command to cancel a booking as a customer.
 *
 * @author Rania, Callan.
 */
public class BookingCancelCommandCustomer implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {

        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter Booking ID: ");
            int bookingIdToCancel = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Type 'confirm' to confirm cancellation, all other inputs will abort operation: ");
            String confirmation = scanner.nextLine();
            if (confirmation.equals("confirm")) {
                cancelBookingAsCustomer(bookingIdToCancel, postgres.getConnection());
                System.out.println("Operation success, booking cancelled.");
            } else {
                System.out.println("Cancellation operation aborted by user.");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), booking operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Bad user input, cancellation operation aborted.");
        }
    }

    public void cancelBookingAsCustomer(int bookingIdToCancel, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call cancel_booking_c(?,?)}");
        statement.setInt(1, bookingIdToCancel);
        statement.setInt(2, Application.user.getID());
        statement.executeUpdate();

    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.CUSTOMER;
    }

    @Override
    public String getDescription() {
        return "Cancel booking as customer";
    }
}
