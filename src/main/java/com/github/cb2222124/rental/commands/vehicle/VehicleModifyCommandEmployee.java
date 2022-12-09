package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Employee command to modify a specified vehicle.
 *
 * @author Rania, Callan.
 */
public class VehicleModifyCommandEmployee implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            //Check for arguments.
            if (!args.containsKey("reg") && !args.containsKey("fee")) {
                System.out.println("No modification argument specified (-reg, -fee), aborting operation.");
                return;
            }

            //Take vehicle ID.
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Vehicle ID: ");
            int vehicleID = scanner.nextInt();
            scanner.nextLine();

            //Change reg.
            if (args.containsKey("reg")) {
                System.out.print("Enter new reg: ");
                String newReg = scanner.nextLine();
                changeVehicleReg(vehicleID, newReg, postgres.getConnection());
                System.out.println("Vehicle registration updated.");
            }

            //Change daily fee.
            if (args.containsKey("fee")) {
                System.out.print("Enter new daily fee: ");
                double newDailyFee = scanner.nextDouble();
                changeDailyFee(vehicleID, newDailyFee, postgres.getConnection());
                System.out.println("Vehicle daily fee updated.  ");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), add vehicle operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Input type mismatch, add vehicle operation aborted.");
        }
    }

    /**
     * Calls database function to update the reg of a specified vehicle.
     *
     * @param vehicle_id The vehicle to update.
     * @param newReg     The new reg.
     * @param connection The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void changeVehicleReg(int vehicle_id, String newReg, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call updateReg(?,?)}");
        statement.setString(1, newReg);
        statement.setInt(2, vehicle_id);
        statement.executeUpdate();

    }

    /**
     * Calls database function to update the daily fee of a specified vehicle.
     *
     * @param vehicle_id  The vehicle to update.
     * @param newDailyFee The new daily fee.
     * @param connection  The Postgres connection to execute command on.
     * @throws SQLException Database errors.
     */
    public void changeDailyFee(int vehicle_id, double newDailyFee, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call updateDailyPrice(?,?)}");
        statement.setDouble(1, newDailyFee);
        statement.setInt(2, vehicle_id);
        statement.executeUpdate();
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Modify vehicle with specified operations.";
    }
}
