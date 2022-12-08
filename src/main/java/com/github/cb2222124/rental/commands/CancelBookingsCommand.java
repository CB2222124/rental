package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;


/**
 * @author Liam
 */
public class CancelBookingsCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        try {
            Postgres postgres = new Postgres();
            boolean cancelBooking = args.containsKey("cancellation");
            if (cancelBooking) {
                int bookingIDCancel = cancelABooking(postgres.getConnection());
                confirmCancellation(bookingIDCancel, postgres.getConnection());
            } else {
/*
                // select * from
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking");
               // ResultSet result = statement.executeQuery();
               // if (result.isBeforeFirst()) {
              //      String[] resultColumns = {"booking_id", "customer_id", "vehicle_id", "pickup_loc", "dropoff_loc", "datefrom", "dateto"};
              //      String[] outputColumns = {"Booking ID", "Customer ID", "Vehicle ID", "Pickup Location ID", "Drop off Location ID", "Collection Date", "Return Date"};
                    new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
                }
          */  }
        }catch(SQLException e){
            System.out.print("Error connecting to database, action aborted.");
            }
        }
    }
    public int cancelABooking(Connection connection) throws SQLException { //function to return int?
            Scanner scanner = new Scanner(System.in);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking");
            ResultSet result = statement.executeQuery();
            if (result.isBeforeFirst()) {
                String[] resultColumns = {"booking_id", "customer_id", "vehicle_id", "pickup_loc", "dropoff_loc", "datefrom", "dateto"};
                String[] outputColumns = {"Booking ID", "Customer ID", "Vehicle ID", "Pickup Location ID", "Drop off Location ID", "Collection Date", "Return Date"};
                new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
                System.out.print("Enter Booking ID for cancellation: ");
                int bookingIDCancel  = scanner.nextInt();
                return bookingIDCancel;
            } else {
                System.out.println("No bookings found.");
            }
        }

    public void confirmCancellation(int bookingID, Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Enter 'yes' to confirm cancellation of Booking ID %f: ", bookingID);
        String confirmation = scanner.nextLine();

        if(confirmation.equalsIgnoreCase("yes")) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM booking WHERE booking_id = ?");
            statement.setInt(1, bookingID); //??
            statement.executeUpdate();
            System.out.print("Booking ID %f : Cancellation confirmed.");
        } else {
        System.out.println("Cancellation aborted.");
        }
    }


    @Override
    public boolean isAvailable() { return Application.user.getRole() == Role.EMPLOYEE; }



    @Override
    public String getDescription() {
        return null;
    }
}
