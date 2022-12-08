package com.github.cb2222124.rental.commands.vehicle;

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
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Rania, Callan
 */
public class ViewVehiclesCommandCustomer implements Command {


    @Override
    public void execute(HashMap<String, String> args) {

        try {
            Postgres postgres = new Postgres();
            Scanner scanner = new Scanner(System.in);

            String city = args.getOrDefault("city", "");
            String country = args.getOrDefault("country", "");

            //Try to find addresses matching user input for city first, then country (if specified).
            if (!city.isBlank()) {
                String cityCode = getCityCode(city, postgres.getConnection());
                showAddresses("city_code", cityCode, postgres.getConnection());
            } else if (!country.isBlank()) {
                String countryCode = getCountryCode(country, postgres.getConnection());
                showAddresses("country_code", countryCode, postgres.getConnection());
            } else {
                showAddresses(postgres.getConnection());
            }

            //Take address ID from user and find matching location.
            System.out.print("Enter address ID: ");
            int addressID = scanner.nextInt();
            int locationID = getLocationID(addressID, postgres.getConnection());
            //Show available vehicles at the given location.
            showAvailableVehicles(locationID, postgres.getConnection());
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Error connecting to database, search aborted.");
        } catch (NoSuchElementException e) {
            if (e.getMessage() == null) {
                System.out.println("Invalid input, search aborted.");
            } else {
                System.out.println(e.getMessage() + ", search aborted.");
            }
        }

    }

    public String getCityCode(String cityName, Connection connection) throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement("SELECT city_code FROM city WHERE city = ? ");
        statement.setString(1, cityName);
        ResultSet result = statement.executeQuery();
        if (result.next()) return result.getString("city_code");
        throw new NoSuchElementException("No addresses found for given city");
    }

    public String getCountryCode(String countryName, Connection connection) throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement("SELECT country_code FROM country WHERE country = ? ");
        statement.setString(1, countryName);
        ResultSet result = statement.executeQuery();
        if (result.next()) return result.getString("country_code");
        throw new NoSuchElementException("No addresses found for given country");
    }

    public void showAddresses(String codeName, String codeValue, Connection connection) throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM address WHERE " + codeName + " = ? ");
        statement.setString(1, codeValue);
        ResultSet result = statement.executeQuery();
        if (!result.isBeforeFirst()) throw new NoSuchElementException("No addresses found for given arguments");
        String[] resultColumns = {"address_id", "num", "street", "city_code", "country_code", "postcode"};
        String[] outputColumns = {"Address ID", "Property Name/Number", "Street", "City Code", "Country Code", "Post Code"};
        new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
    }

    public void showAddresses(Connection connection) throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM address");
        ResultSet result = statement.executeQuery();
        if (!result.isBeforeFirst()) throw new NoSuchElementException("No addresses available.");
        String[] resultColumns = {"address_id", "num", "street", "city_code", "country_code", "postcode"};
        String[] outputColumns = {"Address ID", "Property Name/Number", "Street", "City Code", "Country Code", "Post Code"};
        new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
    }

    public int getLocationID(int addressID, Connection connection) throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement("SELECT location_id FROM location WHERE address_id = ? ");
        statement.setInt(1, addressID);
        ResultSet result = statement.executeQuery();
        if (result.next()) return result.getInt("location_id");
        throw new NoSuchElementException("No location found for given address");
    }

    private void showAvailableVehicles(int locationID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle WHERE location_id = ? AND available = true");
        statement.setInt(1, locationID);
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "reg", "make", "model", "available", "location_id", "daily_fee"};
            String[] outputColumns = {"Vehicle ID", "Registration", "Make", "Model", "Available", "Location ID", "Daily Rate"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No vehicles found.");
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.CUSTOMER;
    }

    @Override
    public String getDescription() {
        return "View vehicles available at specific location.";
    }
}
