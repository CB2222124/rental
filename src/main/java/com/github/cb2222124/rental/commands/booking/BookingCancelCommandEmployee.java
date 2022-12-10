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
 * Command used to cancel an active booking as an Employee.
 *
 * @author Liam.
 */
public class BookingCancelCommandEmployee implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter Booking ID: ");
             int bookingIDCancel = scanner.nextInt();
             scanner.nextLine();
             System.out.print("Type 'confirm' to confirm cancellation, all other inputs will abort operation: ");
             String bookingCancelConfirm = scanner.nextLine();
             confirmCancellation(bookingIDCancel, bookingCancelConfirm, postgres.getConnection());
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, operation aborted.");
        }
    }

    /**
     * Employee function to remove a booking provided it is present.
     *
     * @param bookingID  The booking to cancel.
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void confirmCancellation(int bookingID, String inputConfirmation, Connection connection) throws SQLException {
        String confirmation = inputConfirmation;
        if (confirmation.equalsIgnoreCase("confirm")) {
            CallableStatement statement = connection.prepareCall("{call cancel_booking_e(?)}");
            statement.setInt(1, bookingID);
            statement.executeUpdate();
            System.out.print("Cancellation success.");
        } else {
            System.out.println("Cancellation failure.");
        }
    }


    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }


    @Override
    public String getDescription() {
        return "Cancel specified booking.";
    }
}


