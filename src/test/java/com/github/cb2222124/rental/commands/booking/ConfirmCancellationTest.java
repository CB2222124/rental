package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.commands.CommandTest;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *Testing Employee Booking Cancellation
 *@author Liam
 */


public class ConfirmCancellationTest extends CommandTest {



    @Test
    public void cancelValidBooking() throws SQLException {
        BookingCancelCommandEmployee bookingCancelCommandEmployee = new BookingCancelCommandEmployee();
        bookingCancelCommandEmployee.confirmCancellation(2, "confirm", connection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOKING WHERE BOOKING_ID = 2;");
        assertFalse(statement.executeQuery().isBeforeFirst());
    }

    @Test
    public void invalidConfirmation() throws SQLException {
        BookingCancelCommandEmployee bookingCancelCommandEmployee = new BookingCancelCommandEmployee();
        bookingCancelCommandEmployee.confirmCancellation(2, "fuewbfiywefb", connection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOKING WHERE BOOKING_ID = 2;");
        assertTrue(statement.executeQuery().isBeforeFirst());
    }

    @Test
    public void throwsOnInvalidDetails() throws SQLException {
        BookingCancelCommandEmployee bookingCancelCommandEmployee = new BookingCancelCommandEmployee();
        assertThrows(SQLException.class, () -> bookingCancelCommandEmployee.confirmCancellation(99,"confirm", connection));
    }
}