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
public class ChangeVehicleRegTest extends CommandTest {

    @Test
    public void changeReg() throws SQLException {
        VehicleModifyCommand vehicleModifyCommand = new VehicleModifyCommand();
        vehicleModifyCommand.changeVehicleReg(1, "ABC 123", connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle WHERE vehicle_id = ? AND reg = ?");
        statement.setInt(1, 1);
        statement.setString(2, "ABC 123");

        ResultSet result = statement.executeQuery();
        assertTrue(result.isBeforeFirst());
    }

    @Test
    public void throwsOnInvalidVehicle() {
        assertThrows(PSQLException.class, () -> {
            VehicleModifyCommand vehicleModifyCommand = new VehicleModifyCommand();
            vehicleModifyCommand.changeVehicleReg(100, "ABC 123", connection);
        });
    }

    @Test
    public void throwsOnNoReg() {
        assertThrows(PSQLException.class, () -> {
            VehicleModifyCommand vehicleModifyCommand = new VehicleModifyCommand();
            vehicleModifyCommand.changeVehicleReg(1, null, connection);
        });
    }
}
