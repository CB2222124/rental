package com.github.cb2222124.rental.commands.register;

import com.github.cb2222124.rental.commands.RegisterCommand;
import com.github.cb2222124.rental.commands.CommandTest;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *Testing valid customer registrations and invalid requests
 *@author Liam
 */

public class AddNewCustomerTest extends CommandTest{


    @Test
    public void addsNewCustomerFirstname()throws SQLException{
        String firstname="uniquefirstname";
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.addNewCustomer("uniquefirstname","uniqueusername","uniqueuser","uniquepassword",connection);

        PreparedStatement statement=connection.prepareStatement("SELECT * FROM customer WHERE firstname = ?;");
        statement.setString(1,firstname);
        statement.executeQuery();
        ResultSet results=statement.executeQuery();
        List<String>sqlResult=new ArrayList<>();
        while(results.next()){
        sqlResult.add(results.getString("firstname"));
        }
        List<String>expectedResult=Arrays.asList("uniquefirstname");
        assertEquals(expectedResult,sqlResult);
        }


        @Test

        public void addsNewCustomerLastname()throws SQLException{
        String lastname="uniquelastname";
        RegisterCommand registerCommand =new RegisterCommand();
        registerCommand.addNewCustomer("uniquefirstname","uniquelastname","uniqueuser","uniquepassword",connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE lastname = ?;");
        statement.setString(1,lastname);
        statement.executeQuery();
        ResultSet results=statement.executeQuery();
        List<String>sqlResult = new ArrayList<>();
        while(results.next()){
        sqlResult.add(results.getString("lastname"));
        }
        List<String>expectedResult=Arrays.asList("uniquelastname");
        assertEquals(expectedResult,sqlResult);
        }


        @Test
        public void addsNewCustomerUsername() throws SQLException{
        RegisterCommand registerCommand= new RegisterCommand();
        registerCommand.addNewCustomer("uniquefirstname","uniquelastname","uniqueuser","uniquepassword",connection);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE username = 'uniqueuser';");
        statement.executeQuery();
        ResultSet results = statement.executeQuery();
        List<String>sqlResult = new ArrayList <> ();
        while(results.next()){
        sqlResult.add(results.getString("username"));
        }
        List<String>expectedResult=Arrays.asList("uniqueuser");
        assertEquals(expectedResult,sqlResult);
        }


        @Test
        public void addsNewCustomerPassword()throws SQLException{
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.addNewCustomer("uniquefirstname","uniquelastname","uniqueuser","uniquepassword",connection);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE password = 'uniquepassword';");
        statement.executeQuery();
        ResultSet results = statement.executeQuery();
        List<String>sqlResult = new ArrayList <> ();
        while(results.next()){
        sqlResult.add(results.getString("password"));
        }
        List<String>expectedResult=Arrays.asList("uniquepassword");
        assertEquals(expectedResult,sqlResult);
        }


        @Test

        public void throwOnUsernameTooLong() throws SQLException{
        assertThrows(SQLException.class,()->{
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.addNewCustomer("bob","stevens","thisusernameistoolongforthisapplicationtobevalid,themaximumcapacityisfiftycharactersvarying","password",connection);
        });
        }


        @Test

        public void throwOnPasswordTooLong() throws SQLException{
        assertThrows(SQLException.class,()->{
        RegisterCommand registerCommand =new RegisterCommand();
        registerCommand.addNewCustomer("bob","stevens","bobstevensisthebest","thispasswordistoolong,howdoyouevensupposetoremeberit?1010101010101010101",connection);
        });
        }


        @Test

        public void throwOnUsernameNotUnique() throws SQLException{
        assertThrows(SQLException.class,()->{
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.addNewCustomer("bob","stevens","bob1","password",connection);
        registerCommand.addNewCustomer("bob","jones","bob1","letmein",connection);
        });
        }

        }
