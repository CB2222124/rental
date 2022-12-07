package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.HashMap;

/**
 * Command used to register a customer account.
 * TODO: Implement.
 *
 * @author Callan.
 */
public class RegisterCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        //TODO: Implement.
    }

    @Override
    public boolean isAvailable() {
        return Application.role == Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Create a customer account with provided credentials.";
    }
}
