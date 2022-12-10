package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.commands.CommandTest;
import com.github.cb2222124.rental.commands.LoginCommand;
import com.github.cb2222124.rental.commands.booking.BookingCancelCommandCustomer;
import com.github.cb2222124.rental.commands.booking.BookingDropoffCommand;
import com.github.cb2222124.rental.commands.vehicle.VehicleRemoveCommand;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDropoffCommandTest extends CommandTest {

    @Test
    public void removeBookingValid() throws SQLException {
        BookingDropoffCommand recordDropOff = new BookingDropoffCommand();
        recordDropOff.recordDropoff(2, connection);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT FROM booking WHERE booking_id = 2 AND with_customer = 'true' ");
        assertFalse(statement.executeQuery().isBeforeFirst());

    }

    @Test
    public void removeBookingInvalid() throws SQLException{

        BookingDropoffCommand recordDropOff = new BookingDropoffCommand();
        assertFalse(recordDropOff.recordDropoff(1,connection));

    }

    @Test
    public void removeBookingValid2() throws SQLException{

        BookingDropoffCommand recordDropOff = new BookingDropoffCommand();
        assertFalse(recordDropOff.recordDropoff(4,connection));

    }

}
