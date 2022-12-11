package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.commands.booking.BookingAddCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.ParseException;

public class ValidateDateInputTest extends CommandTest{

    /**
     * Tests date input when trying to book a vehicle
     * @author Rania
     * @throws ParseException checks if invalid string is entered (string that isn't date or isn't in the right format)
     */
    @Test
    public void validDate() throws ParseException {
        BookingAddCommand addBooking = new BookingAddCommand();
        assertTrue(addBooking.validateBookingDates("2023-02-12", "2023-02-13"));

    }

    @Test
    public void validateIfPickupDateIsBeforeToday() throws ParseException {
        BookingAddCommand addBooking = new BookingAddCommand();
        assertFalse(addBooking.validateBookingDates("2022-09-02", "2022-09-15"));
    }

    @Test
    public void validateIfDropOffIsBeforePickup() throws ParseException {
        BookingAddCommand addBooking = new BookingAddCommand();
        assertFalse(addBooking.validateBookingDates("2022-12-15", "2022-12-10"));
    }

    @Test
    public void inValidDateFormat() {
        assertThrows(ParseException.class, () -> {
            BookingAddCommand addBooking = new BookingAddCommand();
            assertFalse(addBooking.validateBookingDates("2023/01/12", "2023/02/15"));
        });
    }

    @Test
    public void inValidDateFormat2() {
        assertThrows(ParseException.class, () -> {
            BookingAddCommand addBooking = new BookingAddCommand();
            assertFalse(addBooking.validateBookingDates("Jan 15th 2023", "Jan 20th 2023"));
        });
    }


}
