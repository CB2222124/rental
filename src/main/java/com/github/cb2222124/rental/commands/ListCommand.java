package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListCommand implements Command {

    @Override
    public void execute() {
        LinkedHashMap<String, Command> commands = Application.commands;

        int longestCommand = 0;
        for (Map.Entry<String, Command> command : commands.entrySet()) {
            if (command.getValue().isAvailable()) {
                if (command.getKey().length() > longestCommand) {
                    longestCommand = command.getKey().length();
                }
            }
        }

        for (Map.Entry<String, Command> command : commands.entrySet()) {
            if (command.getValue().isAvailable()) {
                System.out.format("%-" + (longestCommand + 4) + "s%s\n",
                        command.getKey(), command.getValue().getDescription());
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getDescription() {
        return "List currently available commands.";
    }
}
