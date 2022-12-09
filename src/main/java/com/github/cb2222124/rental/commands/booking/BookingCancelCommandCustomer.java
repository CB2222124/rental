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

public class BookingCancelCommandCustomer implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {

        try{
            Postgres postgres = new Postgres();
            Scanner scanner = new Scanner(System.in);

            System.out.println("Please enter booking ID you would like to cancel");
            int bookingIdToCancel = Integer.parseInt(scanner.nextLine());

            System.out.println("Are you sure you want to cancel booking: " + bookingIdToCancel + " ? Enter 'Yes' or 'No'");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("Yes")){

            cancelBookingAsCustomer(bookingIdToCancel, postgres.getConnection());
                postgres.getConnection().close();
                System.out.println("Booking Cancelled");
            }
            else {
                System.out.println("Cancellation not complete");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void cancelBookingAsCustomer(int bookingIdToCancel, Connection connection)throws SQLException {
        CallableStatement statement = connection.prepareCall("{call cancel_booking_c(?,?)}");
        statement.setInt(1,bookingIdToCancel);
        statement.setInt(2,Application.user.getID());
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
