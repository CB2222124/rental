package com.github.cb2222124.rental;

import com.github.cb2222124.rental.commands.*;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class Application {

    public static Role role = Role.NONE;
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
        while (true) {
            String input = scanner.nextLine();
            Command command = commands.get(input.toLowerCase());
            if (command != null && command.isAvailable()) command.execute();
            else System.out.println("'" + input + "'" + " is not a valid command. Try 'commands'.");
            System.out.println();
        }
    }
}