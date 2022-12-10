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
public class ChangeVehicleFeeTest extends CommandTest {

    @Test
    public void changeFee() throws SQLException {
        VehicleModifyCommand vehicleModifyCommand = new VehicleModifyCommand();
        vehicleModifyCommand.changeDailyFee(1, 500.00, connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle WHERE vehicle_id = ? AND daily_fee = ?");
        statement.setInt(1, 1);
        statement.setDouble(2, 500.00);

        ResultSet result = statement.executeQuery();
        assertTrue(result.isBeforeFirst());
    }

    @Test
    public void throwsOnInvalidVehicle() {
        assertThrows(PSQLException.class, () -> {
            VehicleModifyCommand vehicleModifyCommand = new VehicleModifyCommand();
            vehicleModifyCommand.changeDailyFee(100, 500.00, connection);
        });
    }

    @Test
    public void throwsOnNegativeFee() {
        assertThrows(PSQLException.class, () -> {
            VehicleModifyCommand vehicleModifyCommand = new VehicleModifyCommand();
            vehicleModifyCommand.changeDailyFee(1, -100.00, connection);
        });
    }
}
