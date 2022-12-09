package com.github.cb2222124.rental.commands;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.utils.OutputFormatter;

import java.util.*;

/**
 * Command used to list all currently available commands.
 *
 * @author Callan.
 */
public class ListCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        List<List<String>> table = new ArrayList<>();
        for (Map.Entry<String, Command> command : Application.commands.entrySet()) {
            if(!command.getValue().isAvailable()) continue;
            List<String> row = new ArrayList<>();
            row.add(command.getKey());
            row.add(command.getValue().getDescription());
            table.add(row);
        }
        new OutputFormatter().printTable(table);
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
