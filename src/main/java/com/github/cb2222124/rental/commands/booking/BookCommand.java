package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class BookCommand implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter Vehicle ID: ");
            int vehicleID = scanner.nextInt();

            showLocations(postgres.getConnection());
            System.out.print("Enter Drop-off Location ID: ");
            int dropoffID = scanner.nextInt();

            //TODO: Take more customer details here in a future release.

            System.out.print("Enter Pick-up Date: ");
            String pickupDate = scanner.next();
            System.out.print("Enter Drop-off Date: ");
            String dropoffDate = scanner.next();
            if (!validateBookingDates(pickupDate, dropoffDate)) {
                System.out.println("Invalid Dates, Booking operation aborted (Pick-up Date cannot be before today, Drop-off Date cannot be before Pick-up Date).");
                return;
            }

            System.out.print("Type 'confirm' to confirm booking, all other inputs will abort operation: ");
            if (scanner.next().equals("confirm")) {
                addBooking(vehicleID, dropoffID, java.sql.Date.valueOf(pickupDate),
                        java.sql.Date.valueOf(dropoffDate), postgres.getConnection());
                System.out.println("Booking successful! View your active bookings using the 'bookings' command.");
            } else {
                System.out.println("Booking operation aborted by user.");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), booking operation aborted.");
        } catch (InputMismatchException | ParseException e) {
            System.out.println("Bad user input, booking operation aborted.");
        } catch (NoSuchElementException e) {
            System.out.println("No locations or vehicles available, booking operation aborted.");
        }
    }

    private void addBooking(int vehicleID, int dropoff, java.sql.Date dateFrom, java.sql.Date dateTo, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call addBooking(?,?,?,?,?)}");
        statement.setInt(1, Application.user.getID());
        statement.setInt(2, vehicleID);
        statement.setInt(3, dropoff);
        statement.setDate(4, dateFrom);
        statement.setDate(5, dateTo);
        statement.executeUpdate();
    }

    private void showLocations(Connection connection) throws SQLException, NoSuchElementException {
        CallableStatement statement = connection.prepareCall("{call getLocationsWithAddresses()}");
        ResultSet result = statement.executeQuery();
        if (!result.isBeforeFirst()) throw new NoSuchElementException();
        String[] resultColumns = {"location_id", "num", "street", "city_code", "country_code", "postcode"};
        String[] outputColumns = {"Location ID", "Property Name/Num", "Street", "City Code", "Country Code", "Post Code"};
        new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
    }

    private boolean validateBookingDates(String pickup, String dropoff) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date pickupDate = sdf.parse(pickup);
        Date dropoffDate = sdf.parse(dropoff);
        Date today = sdf.parse(LocalDate.now().toString());
        return pickupDate.compareTo(today) >= 0 && dropoffDate.compareTo(pickupDate) >= 0;
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.CUSTOMER;
    }

    @Override
    public String getDescription() {
        return "Book a specified vehicle.";
    }
}
