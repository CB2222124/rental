package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.commands.CommandTest;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Callan.
 */
public class AddNewVehicleTest extends CommandTest {

    @Test
    public void addsNewVehicle() throws SQLException {
        VehicleNewCommand vehicleNewCommand = new VehicleNewCommand();
        vehicleNewCommand.addNewVehicle(
                "ABC 123", "Toyota", "Supra", 1, 100.00, connection);

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM vehicle WHERE reg = ? AND make = ? AND model = ? AND location_id = ? AND daily_fee = ?");
        statement.setString(1, "ABC 123");
        statement.setString(2, "Toyota");
        statement.setString(3, "Supra");
        statement.setInt(4, 1);
        statement.setDouble(5, 100.00);

        ResultSet result = statement.executeQuery();
        assertTrue(result.isBeforeFirst());
    }

    @Test
    public void throwsOnInvalidLocation() {
        assertThrows(PSQLException.class, () -> {
            VehicleNewCommand vehicleNewCommand = new VehicleNewCommand();
            vehicleNewCommand.addNewVehicle(
                    "ABC 123", "Toyota", "Supra", 100, 100.00, connection);
        });
    }

    @Test
    public void throwsOnNoReg() throws SQLException {
        assertThrows(PSQLException.class, () -> {
            VehicleNewCommand vehicleNewCommand = new VehicleNewCommand();
            vehicleNewCommand.addNewVehicle(
                    null, "Toyota", "Supra", 1, 100.00, connection);
        });
    }
}
