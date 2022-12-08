package com.github.cb2222124.rental.commands;

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
 * @author: Liam
 */

public class EmployeeViewAllVehiclesCommand implements Command {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute(HashMap<String, String> args) {
        try {
            Postgres postgres = new Postgres();
            System.out.print("To filter by booked vehicles enter 'booked', otherwise enter any key: ");
            String filter = scanner.nextLine();
            //indicate whether user wants to filter by booked
            boolean userChoice = applyFilter(filter);
            if(!userChoice) {
                try {
                    searchAllVehiclesUnfiltered(postgres.getConnection());
                } catch (NoSuchElementException e){
                        System.out.println("Error");
                }
            } else {
                try {
                    searchAllVehiclesFiltered(postgres.getConnection());
                } catch (NoSuchElementException e){
                        System.out.println("Error");
                }
            }
            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.print("Error connecting to database");
        }
    }

    public boolean applyFilter(String filter){
        return filter.equalsIgnoreCase("booked");
    }

    public void searchAllVehiclesUnfiltered (Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle");
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "reg", "make", "model", "available", "location_id", "daily_fee"};
            String[] outputColumns = {"Vehicle ID", "Registration", "Make", "Model", "Available", "Location ID", "Daily Rate"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No vehicles found.");
        }
    }

    public void searchAllVehiclesFiltered (Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select vehicle.vehicle_id, vehicle.reg, vehicle.make, vehicle.model, booking.customer_id, booking.datefrom, booking.dateto FROM vehicle INNER JOIN booking ON booking.vehicle_id = vehicle.vehicle_id ORDER BY vehicle.vehicle_id;");
        ResultSet result = statement.executeQuery();
        if (result.isBeforeFirst()) {
            String[] resultColumns = {"vehicle_id", "reg", "make", "model", "customer_id", "datefrom", "dateto"};
            String[] outputColumns = {"Vehicle ID", "Registration", "Make", "Model", "Customer ID", "Date booking starts", "Date booking ends"};
            new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
        } else {
            System.out.println("No vehicles found.");
        }
    }

    @Override
    public boolean isAvailable() { return Application.user.getRole() == Role.EMPLOYEE; }

    @Override
    public String getDescription() { return "Search all available vehicles with an option to filter by booked"; }

}
