package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.models.Command;

import java.util.HashMap;

/**
 * Command used to exit the application.
 *
 * @author Callan.
 */
public class ExitCommand implements Command {
    @Override
    public void execute(HashMap<String, String> args) {
        System.exit(0);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Exit the application.";
    }
}
