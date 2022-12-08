package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import com.github.cb2222124.rental.utils.Postgres;

import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Rania allows employees to modify a selected vehicle
 */
public class ModifyVehiclesCommandEmployee implements Command {
    @Override
    public void execute(HashMap<String, String> args) {

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


                System.out.print("Please enter new reg:");
                String newReg = scanner.next();

                changeVehicleReg(vehicleID, newReg, postgres.getConnection());
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
                postgres.getConnection().close();
            }
        } catch (SQLException e) {
            System.out.print("Error connecting to database, search aborted.");

        }


    }

    public void changeVehicleReg(int vehicle_id, String newReg, Connection connection)throws SQLException{
        CallableStatement statement = connection.prepareCall("{call updateReg(?,?)}");
        statement.setString(1,newReg);
        statement.setInt(2,vehicle_id);
        statement.executeUpdate();


    }

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
