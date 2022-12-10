package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.*;
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

            System.out.print("Enter Booking ID: ");
            int bookingIdToCancel = scanner.nextInt();
            scanner.nextLine();
            if(checkBookingBelongsToCustomer(bookingIdToCancel,Application.user.getID(), postgres.getConnection())) {

                System.out.print("Type 'confirm' to confirm cancellation, all other inputs will abort operation: ");
                String confirmation = scanner.nextLine();
                if (confirmation.equals("confirm")) {
                    cancelBookingAsCustomer(bookingIdToCancel, Application.user.getID(), postgres.getConnection());
                    System.out.println("Operation success, booking cancelled.");
                } else {
                    System.out.println("Cancellation operation aborted by user.");
                }
            }
            else{
                System.out.println("Sorry this booking does not exist. Exiting..");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), booking operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Bad user input, cancellation operation aborted.");
        }
    }

    /**
     * <p>
     *     This function takes the customer's ID and the booking ID they want to cancel providing they have not picked
     *     up the vehicle yet.
     *     Customer_ID must match logged in customer (validated above, line 34)
     *     Call made to SQL function - cancel_booking - that validates booking and vehicle possession
     *
     * @param bookingIdToCancel The booking to cancel.
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void cancelBookingAsCustomer(int bookingIdToCancel, int customer_id, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call cancel_booking_c(?,?)}");
        statement.setInt(1, bookingIdToCancel);
        statement.setInt(2, customer_id);
        statement.executeUpdate();

    }

    public boolean checkBookingBelongsToCustomer(int booking_id, int customer_id, Connection connection) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE booking_id = ? AND customer_id = ?");
        statement.setInt(1,booking_id);
        statement.setInt(2,customer_id);

        ResultSet results = statement.executeQuery();
        if(results.isBeforeFirst()){
            return true;
        }
        return false;
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.CUSTOMER;
    }

    @Override
    public String getDescription() {
        return "Cancel specified booking.";
    }
}
