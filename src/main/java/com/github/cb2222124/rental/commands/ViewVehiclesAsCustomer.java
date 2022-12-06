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

public class ViewVehiclesAsCustomer implements Command {
    @Override
    public void execute() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a country: ");
        String country = scanner.nextLine();
        System.out.print("Please enter a city: ");
        String city = scanner.nextLine();

        try {
            Postgres postgres = new Postgres();
            searchVehicle(country,city, postgres.getConnection());
                System.out.printf("Available cars in %s %s : ", country, city);


            postgres.getConnection().close();
        } catch (SQLException e) {
            System.out.println("No available vehicles in specified location.");
        }

    }

/* SELECT * FROM vehicle right join location ON location.location_id= vehicle.location_id
 */
    private boolean searchVehicle(String country, String city, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM location WHERE country = ? AND city = ?");

        statement.setString(1, country);
        statement.setString(2, city);

        ResultSet result = statement.executeQuery();
        return result.next();
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
