package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.LinkedHashMap;

/**
 * Parent command to run vehicle sub commands.
 * Arguments: -add New vehicle command.
 * -delete Delete vehicle command.
 * -modify Modify vehicle command.
 *
 * @author Callan.
 */
public class VehicleCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        String command = "";
        for (String arg : args.keySet()) {
            if (arg.equals("add") || arg.equals("delete") || arg.equals("modify")) {
                command = arg;
                break;
            }
        }
        switch (command) {
            case "add" -> new VehicleNewCommand().execute(args);
            case "delete" -> new VehicleRemoveCommand().execute(args);
            case "modify" -> new VehicleModifyCommand().execute(args);
            default -> System.out.println("No valid vehicle operation argument provided (-add, -delete, -modify).");
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() == Role.EMPLOYEE;
    }

    @Override
    public String getDescription() {
        return "Perform operations on a specific vehicle.";
    }
}

