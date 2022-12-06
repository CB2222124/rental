package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.Scanner;

public class LoginCommand implements Command {

    @Override
    public void execute() {
        //TODO: Replace with actual login logic.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.equals("user") && password.equals("password")) {
            Application.role = Role.CUSTOMER;
            System.out.println("Customer login successful.");
        } else if (username.equals("admin") && password.equals("password")) {
            Application.role = Role.EMPLOYEE;
            System.out.println("Employee login successful.");
        } else {
            System.out.println("Invalid credentials, login aborted.");
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.role == Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Log into the application with provided credentials.";
    }
}
