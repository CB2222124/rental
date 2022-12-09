package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import com.github.cb2222124.rental.utils.Postgres;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * @author Rania allows employees to modify a selected vehicle
 */
public class VehicleModifyCommandEmployee implements Command {
    @Override
    public void execute(LinkedHashMap<String, String> args) {

        try{
            if(args.isEmpty()){
                System.out.println("1:To modify vehicle reg type 'modify -reg'");
                System.out.println("2:To modify vehicle daily fee type 'modify -dailyFee'");
            }
            else if(args.containsKey("reg")) {

                Postgres postgres = new Postgres();
                Scanner scanner = new Scanner(System.in);

                System.out.println("Please enter vehicle ID: ");
                int vehicleID = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Please enter new reg: ");
                String newReg = scanner.nextLine();

                changeVehicleReg(vehicleID, newReg, postgres.getConnection());
                System.out.println("Registration for vehicle:" + vehicleID +" updated successfully");
                postgres.getConnection().close();
            }
            else if(args.containsKey("dailyFee")){
                Postgres postgres = new Postgres();
                Scanner scanner = new Scanner(System.in);

                System.out.println("Please enter vehicle ID:\n ");
                int vehicleID = scanner.nextInt();

                System.out.println("Please enter new daily fee: ");
                double newDailyFee = scanner.nextDouble();

                changeDailyFee(vehicleID, newDailyFee, postgres.getConnection());
                System.out.println("Daily fee for vehicle :" + vehicleID +" updated successfully");
                postgres.getConnection().close();
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());

        }

    }

    /**
     * <p>
     *     Function that makes call to database function to update a selected vehicles reg number
     * @param vehicle_id taken from user to allow to search for a specific vehicle
     * @param newReg taken from user to update reg of selected vehicle
     */
    public void changeVehicleReg(int vehicle_id, String newReg, Connection connection)throws SQLException{
        CallableStatement statement = connection.prepareCall("{call updateReg(?,?)}");
        statement.setString(1,newReg);
        statement.setInt(2,vehicle_id);
        statement.executeUpdate();

    }

    /**
     * <p>
     *      *     Function that makes call to database function to update a selected vehicles daily fee
     * @param vehicle_id taken from user to allow to search for a specific vehicle
     * @param newDailyFee taken from user to insert new value into daily fee of selected vehicle
     */
    public void changeDailyFee(int vehicle_id, double newDailyFee, Connection connection)throws SQLException{

        CallableStatement statement = connection.prepareCall("{call updateDailyPrice(?,?)}");
        statement.setDouble(1,newDailyFee);
        statement.setInt(2,vehicle_id);
        statement.executeUpdate();
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Modify vehicle must specify arguments";
    }
}
