package com.github.cb2222124.rental.commands.location;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

/**
 * Command to view all rental locations.
 *
 * @author Callan
 */
public class ViewLocationsCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            showLocations(postgres.getConnection());
        } catch (SQLException e) {
            System.out.print("Error connecting to database, location search aborted.");
        }
    }

    /**
     * Outputs a table constructed from a location, and it's associated address.
     *
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException           Database errors.
     * @throws NoSuchElementException No locations were found.
     */
    private void showLocations(Connection connection) throws SQLException, NoSuchElementException {
        CallableStatement statement = connection.prepareCall("{call getLocationsWithAddresses()}");
        ResultSet result = statement.executeQuery();
        if (!result.isBeforeFirst()) throw new NoSuchElementException();
        String[] resultColumns = {"location_id", "num", "street", "city_code", "country_code", "postcode"};
        String[] outputColumns = {"Location ID", "Property Name/Num", "Street", "City Code", "Country Code", "Post Code"};
        new OutputFormatter().printResultSet(result, resultColumns, outputColumns);
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() != Role.NONE;
    }

    @Override
    public String getDescription() {
        return "View all rental locations.";
    }
}

