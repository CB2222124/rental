package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookingRecordDropoffCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Booking ID: ");
            if (recordPickup(scanner.nextInt(), postgres.getConnection())) {
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

    public boolean recordPickup(int bookingID, Connection connection) throws SQLException {
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
