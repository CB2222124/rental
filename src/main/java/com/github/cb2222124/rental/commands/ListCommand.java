package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Command used to list all currently available commands.
 *
 * @author Callan.
 */
public class ListCommand implements Command {

    @Override
    public void execute(HashMap<String, String> args) {
        LinkedHashMap<String, Command> commands = Application.commands;

        //First pass is to establish how much padding the command names require.
        int longestCommand = 0;
        for (Map.Entry<String, Command> command : commands.entrySet()) {
            if (command.getValue().isAvailable()) {
                if (command.getKey().length() > longestCommand) {
                    longestCommand = command.getKey().length();
                }
            }
        }

        //Now lets print out every command.
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
