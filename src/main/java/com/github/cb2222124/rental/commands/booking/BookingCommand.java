package com.github.cb2222124.rental.commands.booking;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.LinkedHashMap;

/**
 * Parent booking command that uses arguments to run sub commands.
 * Arguments: -new New customer booking command.
 * -cancel Cancel booking command.
 * -change Change booking command.
 * -pickup Record customer pickup command.
 * -dropoff Record customer dropoff command.
 * TODO: Refactor construction of sub commands in the future.
 *
 * @author Callan.
 */
public class BookingCommand implements Command {

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        String command = "";
        for (String arg : args.keySet()) {
            if (arg.equals("new") || arg.equals("cancel") || arg.equals("change") || arg.equals("pickup") || arg.equals("dropoff")) {
                command = arg;
                break;
            }
        }
        switch (command) {
            case "new" -> {
                if (Application.user.getRole() == Role.CUSTOMER) {
                    new BookingAddCommand().execute(args);
                }
            }
            case "cancel" -> {
                if (Application.user.getRole() == Role.EMPLOYEE) {
                    new BookingCancelCommandEmployee().execute(args);
                } else if (Application.user.getRole() == Role.CUSTOMER) {
                    new BookingCancelCommandCustomer().execute(args);
                }
            }
            case "change" -> {
                if (Application.user.getRole() == Role.CUSTOMER) {
                    new BookingChangeVehicleCommand().execute(args);
                }
            }
            case "pickup" -> {
                if (Application.user.getRole() == Role.EMPLOYEE) {
                    new BookingPickupCommand().execute(args);
                }
            }
            case "dropoff" -> {
                if (Application.user.getRole() == Role.EMPLOYEE) {
                    new BookingDropoffCommand().execute(args);
                }
            }
            default -> {
                if (Application.user.getRole() == Role.EMPLOYEE) {
                    System.out.println("No valid booking operation argument provided (-cancel, -pickup, -dropoff).");
                } else {
                    System.out.println("No valid booking operation argument provided (-new, -cancel, -change).");
                }
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() != Role.NONE;
    }

    @Override
    public String getDescription() {
        return "Perform operations on a specific booking.";
    }
}

