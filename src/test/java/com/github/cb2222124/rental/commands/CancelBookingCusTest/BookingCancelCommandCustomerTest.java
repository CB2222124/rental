package com.github.cb2222124.rental.commands.CancelBookingCusTest;
import com.github.cb2222124.rental.commands.booking.BookingCancelCommandCustomer;
import com.github.cb2222124.rental.commands.CommandTest;
import org.junit.jupiter.api.Test;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rania tests cancel booking as a customer and checking exception if invalid
 */

public class BookingCancelCommandCustomerTest extends CommandTest {

    @Test
    public void cancelValidBooking() throws SQLException {

        BookingCancelCommandCustomer cancelValidBooking = new BookingCancelCommandCustomer();
        cancelValidBooking.cancelBookingAsCustomer(1, 2, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM BOOKING WHERE BOOKING_ID = 1 AND customer_id = 2");
        assertFalse(statement.executeQuery().isBeforeFirst());
    }

    @Test
    public void throwsOnCancelInvalidBooking(){
        BookingCancelCommandCustomer cancelInvalidBooking = new BookingCancelCommandCustomer();
        assertThrows(SQLException.class,()->cancelInvalidBooking.cancelBookingAsCustomer(23,4,connection));
    }
}


