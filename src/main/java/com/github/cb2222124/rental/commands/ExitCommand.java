package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.models.Command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
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
