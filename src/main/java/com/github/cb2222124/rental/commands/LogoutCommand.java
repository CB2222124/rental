package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

public class LogoutCommand implements Command {

    @Override
    public void execute() {
        Application.role = Role.NONE;
        System.out.println("Logout successful. Thank you for using Rentals!");
    }

    @Override
    public boolean isAvailable() {
        return Application.role != Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Log out of the application.";
    }
}
