package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

public class RegisterCommand implements Command {

    @Override
    public void execute() {
        //TODO: Replace with actual register logic.
        System.out.println("Registration is not currently available.");
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
