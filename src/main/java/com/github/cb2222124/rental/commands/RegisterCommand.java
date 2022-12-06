package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.utils.Postgres;

import java.util.Scanner;

public class RegisterCommand implements Command {

    @Override
    public void execute() {
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
