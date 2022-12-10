package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.commands.CommandTest;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Callan.
 */
public class DeleteVehicleTest extends CommandTest {

    @Test
    public void deleteVehicle() throws SQLException {
        VehicleRemoveCommand vehicleRemoveCommand = new VehicleRemoveCommand();
        vehicleRemoveCommand.deleteVehicle(1, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle WHERE vehicle_id = ?");
        statement.setInt(1, 1);

        ResultSet result = statement.executeQuery();
        assertFalse(result.isBeforeFirst());
    }

    @Test
    public void trueOnValidVehicle() throws SQLException {
        VehicleRemoveCommand vehicleRemoveCommand = new VehicleRemoveCommand();
        assertTrue(vehicleRemoveCommand.deleteVehicle(1, connection));
    }

    @Test
    public void falseOnInvalidVehicle() throws SQLException {
        VehicleRemoveCommand vehicleRemoveCommand = new VehicleRemoveCommand();
        assertFalse(vehicleRemoveCommand.deleteVehicle(100, connection));
    }

    @Test
    public void throwsOnUnavailableVehicle() {
        assertThrows(PSQLException.class, () -> {
            VehicleRemoveCommand vehicleRemoveCommand = new VehicleRemoveCommand();
            vehicleRemoveCommand.deleteVehicle(3, connection);
        });
    }
}
