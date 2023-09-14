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
 * Employee command to modify a specified sports car.
 * Arguments: -reg: Modify sports car reg.
 * -fee: Modify sports car daily fee.
 */
public class sportsCarEditCommand implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            // Check for arguments.
            if (!args.containsKey("reg") && !args.containsKey("fee")) {
                System.out.println("No modification argument specified (-reg, -fee), aborting operation.");
                return;
            }

            // Take sports car ID.
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Sports Car ID: ");
            int sportsCarID = scanner.nextInt();
            scanner.nextLine();

            // Change reg.
            if (args.containsKey("reg")) {
                System.out.print("Enter new reg: ");
                String newReg = scanner.nextLine();
                changeSportsCarReg(sportsCarID, newReg, postgres.getConnection());
                System.out.println("Sports car registration updated.");
            }

            // Change daily fee.
            if (args.containsKey("fee")) {
                System.out.print("Enter new daily fee: ");
                double newDailyFee = scanner.nextDouble();
                changeDailyFee(sportsCarID, newDailyFee, postgres.getConnection());
                System.out.println("Sports car daily fee updated.");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), sports car modification aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Input type mismatch, sports car modification aborted.");
        }
    }

    /**
     * Calls a database function to update the reg of a specified sports car.
     *
     * @param sportsCarID The sports car to update.
     * @param newReg      The new reg.
     * @param connection  The Postgres connection to execute the command on.
     * @throws SQLException Database errors.
     */
    public void changeSportsCarReg(int sportsCarID, String newReg, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call updateSportsCarReg(?, ?)}");
        statement.setInt(1, sportsCarID);
        statement.setString(2, newReg);
        statement.executeUpdate();
    }

    /**
     * Calls a database function to update the daily fee of a specified sports car.
     *
     * @param sportsCarID  The sports car to update.
     * @param newDailyFee  The new daily fee.
     * @param connection   The Postgres connection to execute the command on.
     * @throws SQLException Database errors.
     */
    public void changeDailyFee(int sportsCarID, double newDailyFee, Connection connection) throws SQLException {
        CallableStatement statement = connection.prepareCall("{call updateSportsCarDailyFee(?, ?)}");
        statement.setInt(1, sportsCarID);
        statement.setDouble(2, newDailyFee);
        statement.executeUpdate();
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Modify sports car with specified operations.";
    }
}
