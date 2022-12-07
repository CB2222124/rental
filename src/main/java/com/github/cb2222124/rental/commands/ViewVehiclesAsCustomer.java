package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

/**
 * author @ Rania
 */
public class ViewVehiclesAsCustomer implements Command {

    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() {

        System.out.println("Enter location ID: \n");
        int locationID = Integer.parseInt(scanner.nextLine());

        try {
            Postgres postgres = new Postgres();
            searchVehicle(locationID,postgres.getConnection());

            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            System.out.println(e);
        }
    }


    private boolean searchVehicle(int locationID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM vehicle WHERE location_id = ? AND available = true");

        statement.setInt(1, locationID);


        ResultSet result = statement.executeQuery();
        printResults(result,"vehicle_id","reg" ,"make","model" , "available", "location_id" , "daily_fee");
        return result.next();
    }

    public boolean getLocation(int addressID, Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "SELECT location_id FROM location WHERE address_id = ? ");
        statement.setInt(1, addressID);


        ResultSet result = statement.executeQuery();

        return result.next();
    }
    public int getAddress(String cityCode, Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM address WHERE city_code = ? ");
        statement.setString(1, cityCode);


        ResultSet result = statement.executeQuery();
        printResults(result, "Address Number", "House Number", "Street", "Postcode");

        System.out.println("Please select address number");
        int addressNumber = Integer.parseInt(scanner.nextLine());
        return addressNumber;
    }

    public String getCityCode(String cityName, Connection connection)throws SQLException {


            PreparedStatement statement = connection.prepareStatement(
                    "SELECT city_code FROM city WHERE city = ? ");
            statement.setString(1, cityName);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String cityCode = result.getString("city_code");
                System.out.println(cityCode);
                return cityCode;
            }
        return null;
    }

    public static void printResults(ResultSet data, String... columnNames) throws SQLException {
        for (String column : columnNames) {
            System.out.printf("%s\t", column);
        }
        System.out.printf("\n");

        while (data.next()) {
            for (String column : columnNames) {
                System.out.printf("%s\t", data.getString(column));
            }
            System.out.printf("\n");
        }
    }



    @Override
    public boolean isAvailable() {
        return Application.role == Role.NONE;
        }

    @Override
    public String getDescription() {
        return "Vehicles available at specific location";
    }
}
