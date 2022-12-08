package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.OutputFormatter;
import com.github.cb2222124.rental.utils.Postgres;

import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class ModifyVehiclesCommandEmployee implements Command {
    @Override
    public void execute(HashMap<String, String> args) {

        try{
            if(args.containsKey("reg")) {

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

                System.out.printf("Please enter new daily fee for vehicle ID: %d", vehicleID);
                double newDailyFee = scanner.nextDouble();

                changeDailyFee(vehicleID, newDailyFee, postgres.getConnection());
                postgres.getConnection().close();
            }
            else{
                System.out.println("Enter modify -reg or modify -dailyFee");
            }
        } catch (SQLException e) {
            System.out.print("Error connecting to database, search aborted.");
            System.out.print(e);
        }


    }

    public void changeVehicleReg(int vehicle_id, String newReg, Connection connection)throws SQLException{
        CallableStatement statement = connection.prepareCall("{call updateReg(?,?)}");
        statement.setString(1,newReg);
        statement.setInt(2,vehicle_id);
        statement.executeUpdate();


    }

    public void changeDailyFee(int vehicle_id, double newDailyFee, Connection connection)throws SQLException{

        PreparedStatement statement = connection.prepareStatement("UPDATE vehicle SET daily_fee =? WHERE vehicle_id = ?");
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
