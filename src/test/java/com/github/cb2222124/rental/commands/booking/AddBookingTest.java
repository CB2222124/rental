package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.commands.CommandTest;
import com.github.cb2222124.rental.commands.vehicle.VehicleNewCommand;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Callan.
 */
public class AddBookingTest extends CommandTest {

    @Test
    public void addNewBooking() throws SQLException {
        BookingAddCommand bookingAddCommand = new BookingAddCommand();
        bookingAddCommand.addBooking(1, 1, 1,
                Date.valueOf("2023-01-01"), Date.valueOf("2023-01-08"), connection);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM booking WHERE customer_id = ? AND vehicle_id = ?");
        statement.setInt(1, 1);
        statement.setInt(2, 1);
        ResultSet resultSet = statement.executeQuery();
        assertTrue(resultSet.isBeforeFirst());
    }

    @Test
    public void throwsOnUnavailableVehicle() {
        assertThrows(PSQLException.class, () -> {
            BookingAddCommand bookingAddCommand = new BookingAddCommand();
            bookingAddCommand.addBooking(1, 3, 1,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-01-08"), connection);
        });
    }

    @Test
    public void throwsOnInvalidVehicle() {
        assertThrows(PSQLException.class, () -> {
            BookingAddCommand bookingAddCommand = new BookingAddCommand();
            bookingAddCommand.addBooking(1, 500, 1,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-01-08"), connection);
        });
    }

    @Test
    public void throwsOnInvalidCustomer() {
        assertThrows(PSQLException.class, () -> {
            BookingAddCommand bookingAddCommand = new BookingAddCommand();
            bookingAddCommand.addBooking(500, 1, 1,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-01-08"), connection);
        });
    }

    @Test
    public void throwsOnInvalidLocation() {
        assertThrows(PSQLException.class, () -> {
            BookingAddCommand bookingAddCommand = new BookingAddCommand();
            bookingAddCommand.addBooking(1, 1, 500,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-01-08"), connection);
        });
    }
}