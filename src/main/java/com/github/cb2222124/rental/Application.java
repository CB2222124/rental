package com.github.cb2222124.rental;

import com.github.cb2222124.rental.commands.*;
import com.github.cb2222124.rental.commands.booking.BookCommand;
import com.github.cb2222124.rental.commands.booking.ViewBookingsCommand;
import com.github.cb2222124.rental.commands.vehicle.ModifyVehiclesCommandEmployee;
import com.github.cb2222124.rental.commands.vehicle.VehicleCommand;
import com.github.cb2222124.rental.commands.vehicle.ViewVehiclesCommand;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;
import com.github.cb2222124.rental.models.User;
import com.github.cb2222124.rental.utils.CommandParser;

import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Application entry point responsible for registering available commands and executing a
 * Read-Evaluate-Print-Loop.
 *
 * @author Callan.
 */
public class Application {

    public static User user = new User(Role.NONE, 0);
    public static final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();

    public static void main(String[] args) {
        registerCommands();
        displayWelcome();
        startREPL();
    }

    /**
     * Register application commands.
     */
    private static void registerCommands() {
        commands.put("register", new RegisterCommand());
        commands.put("login", new LoginCommand());
        commands.put("vehicles", new ViewVehiclesCommand());
        commands.put("vehicle", new VehicleCommand());
        commands.put("modify", new ModifyVehiclesCommandEmployee());
        commands.put("book", new BookCommand());
        commands.put("bookings", new ViewBookingsCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("commands", new ListCommand());
        commands.put("exit", new ExitCommand());
    }

    /**
     * Display welcome message.
     */
    private static void displayWelcome() {
        //https://ascii.today/
        System.out.println("""       
                   ___  _____  ___________   __   ____
                  / _ \\/ __/ |/ /_  __/ _ | / /  / __/
                 / , _/ _//    / / / / __ |/ /___\\ \\ \s
                /_/|_/___/_/|_/ /_/ /_/ |_/____/___/
                                
                Welcome to Rentals! Don't know where to start? Try 'commands'.
                """);
    }

    /**
     * Application read-eval-print-loop (REPL).
     */
    @SuppressWarnings("InfiniteLoopStatement")
    private static void startREPL() {
        Scanner scanner = new Scanner(System.in);
        CommandParser parser = new CommandParser();
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            Command command = commands.get(parser.getCommand(input));
            if (command != null && command.isAvailable()) command.execute(parser.getArguments(input));
            else System.out.println("'" + input + "'" + " is not a valid command. Try 'commands'.");
            System.out.println();
        }
    }
}