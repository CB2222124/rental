package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Rania
 */
public class ViewVehiclesAsCustomer implements Command {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute(HashMap<String, String> args) {
        try {
            Postgres postgres = new Postgres();
            //Take city name from user and show matching addresses.
            System.out.print("Enter city name: ");
            String city = scanner.nextLine();
            String cityCode = getCityCode(city, postgres.getConnection());
            showAddresses(cityCode, postgres.getConnection());
            //Take address ID from user and find matching location.
            System.out.print("Enter address ID: ");
            int addressID = scanner.nextInt();
            int locationID = getLocationID(addressID, postgres.getConnection());
            //Show available vehicles at the given location.
            showAvailableVehicles(locationID, postgres.getConnection());
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Connection failed, search aborted.");
            e.printStackTrace();
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
        throw new NoSuchElementException("No address found for given city");
    }

    public void showAddresses(String cityCode, Connection connection) throws SQLException, NoSuchElementException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM address WHERE city_code = ? ");
        statement.setString(1, cityCode);
        ResultSet result = statement.executeQuery();
        if (!result.isBeforeFirst()) throw new NoSuchElementException("No address found for given city");
        printResults(result, "address_id", "num", "street", "city_code", "country_code", "postcode");
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
            printResults(result, "vehicle_id", "reg", "make", "model", "available", "location_id", "daily_fee");
        } else {
            System.out.println("No vehicles found.");
        }
    }

    public static void printResults(ResultSet data, String... columnNames) throws SQLException {
        for (String column : columnNames) {
            System.out.printf("%s\t", column);
        }
        System.out.print("\n");
        while (data.next()) {
            for (String column : columnNames) {
                System.out.printf("%s\t", data.getString(column));
            }
            System.out.print("\n");
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.CUSTOMER;
    }

    @Override
    public String getDescription() {
        return "Vehicles available at specific location.";
    }
}
