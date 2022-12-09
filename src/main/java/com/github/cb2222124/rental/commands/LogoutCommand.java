package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.LinkedHashMap;

/**
 * Command used to log out of the application.
 *
 * @author Callan.
 */
public class LogoutCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        Application.user.updateUser(Role.NONE, 0);
        System.out.println("Logout successful. Thank you for using Rentals!");
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() != Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Log out of the application.";
    }
}
