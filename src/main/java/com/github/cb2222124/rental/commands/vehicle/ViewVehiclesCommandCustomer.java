package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Rania, Callan
 */
public class ViewVehiclesCommandCustomer implements Command {


    @Override
    public void execute(LinkedHashMap<String, String> args) {

        try {
            Postgres postgres = new Postgres();
            Scanner scanner = new Scanner(System.in);

            //Read optional arguments.
            String city = args.getOrDefault("city", "");
            String country = args.getOrDefault("country", "");
            String make = args.getOrDefault("make", "");
            String model = args.getOrDefault("model", "");

            //Translate city/country names to codes and find matching locations.
            String cityCode = city.isBlank() ? "" : getCityCode(city, postgres.getConnection());
            String countryCode = country.isBlank() ? "" : getCountryCode(country, postgres.getConnection());
            showLocations(cityCode, countryCode, postgres.getConnection());

            //Show vehicles that match users arguments.
            //Note: Currently make model filter is applied once the customer commits to a location (Per the brief),
            //a future iteration might allow the user to find a location that has a certain make.
            System.out.print("Enter Location ID to search: ");
            int locationID = scanner.nextInt();
            showAvailableVehicles(make, model, locationID, postgres.getConnection());

            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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

    private void showLocations(String cityCode, String countryCode, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call locationCityCountryCodeSearch(?, ?)}");
        statement.setString(1, cityCode);
        statement.setString(2, countryCode);
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"location_id", "num", "street", "city_code", "country_code", "postcode"};
            String[] outputColumns = {"Location ID", "Property Name/Num", "Street", "City Code", "Country Code", "Post Code"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No locations found.");
        }
    }

    private void showAvailableVehicles(String make, String model, int locationID, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call customerLocMakeModelSearch(?, ?, ?)}");
        statement.setString(1, make);
        statement.setString(2, model);
        statement.setInt(3, locationID);
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "location_id", "reg", "make", "model", "daily_fee"};
            String[] outputColumns = {"Vehicle ID", "Location ID", "Registration", "Make", "Model", "Daily Rate"};
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
