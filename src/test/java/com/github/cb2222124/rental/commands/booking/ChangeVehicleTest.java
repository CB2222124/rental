package com.github.cb2222124.rental.commands.booking;
import com.github.cb2222124.rental.commands.CommandTest;
import org.junit.jupiter.api.Test;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 *Testing valid and invalid customer requests for a vehicle change on their booking
 *@author Liam
 */
public class ChangeVehicleTest extends CommandTest {


    @Test
    public void changeVehicleBooking() throws SQLException {
        BookingChangeVehicleCommand bookingChangeVehicleCommand = new BookingChangeVehicleCommand();
        bookingChangeVehicleCommand.changeVehicle(1, 10, 2, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE booking_id = 1 AND vehicle_id = 10 AND customer_id = 2;");
        ResultSet result = statement.executeQuery();
        assertTrue(result.isBeforeFirst());
    }


    @Test
    public void incorrectRequirementsForBookingChange() throws SQLException {
        BookingChangeVehicleCommand bookingChangeVehicleCommand = new BookingChangeVehicleCommand();
        bookingChangeVehicleCommand.changeVehicle(2, 9, 1, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE booking_id = 2 AND vehicle_id = 9 AND customer_id = 4;");
        ResultSet result = statement.executeQuery();
        assertFalse(result.isBeforeFirst());
    }

    @Test
    public void incorrectVehicleID() throws SQLException {
        BookingChangeVehicleCommand bookingChangeVehicleCommand = new BookingChangeVehicleCommand();
        bookingChangeVehicleCommand.changeVehicle(4, 999, 6, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE booking_id = 5 AND vehicle_id = 999 AND customer_id = 4;");
        ResultSet result = statement.executeQuery();
        assertFalse(result.isBeforeFirst());
    }

    @Test
    public void customerWithoutACurrentBooking() throws SQLException {
        BookingChangeVehicleCommand bookingChangeVehicleCommand = new BookingChangeVehicleCommand();
        bookingChangeVehicleCommand.changeVehicle(2, 9, 1, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM booking WHERE booking_id = 2 AND vehicle_id = 10 AND customer_id = 1;");
        ResultSet result = statement.executeQuery();
        assertFalse(result.isBeforeFirst());
    }
}






