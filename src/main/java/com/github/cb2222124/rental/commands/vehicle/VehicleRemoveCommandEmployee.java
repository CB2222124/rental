package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * @author Saeed, Callan.
 */
public class VehicleRemoveCommandEmployee implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {
        try (Postgres postgres = new Postgres()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Vehicle ID: ");
            int vehicle_id = scanner.nextInt();
            if (deleteVehicle(vehicle_id, postgres.getConnection()))
                System.out.println("Vehicle deleted successfully.");
            else {
                System.out.println("No vehicle deleted, verify ID and availability.");
            }
        } catch (SQLException e) {
            System.out.println("Database error (" + e.getMessage() + "), delete vehicle operation aborted.");
        } catch (InputMismatchException e) {
            System.out.println("Input type mismatch, delete vehicle operation aborted.");
        }
    }

    public boolean deleteVehicle(int id, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM vehicle WHERE vehicle_id = ?");
        statement.setInt(1, id);
        return statement.executeUpdate() != 0;
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Delete a specific vehicle.";
    }
}
