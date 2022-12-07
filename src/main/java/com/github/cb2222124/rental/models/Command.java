package com.github.cb2222124.rental.models;

import java.util.HashMap;

/**
 * Interface used to specify the behavior commands must implement.
 *
 * @author Callan.
 */
public interface Command {
    void execute(HashMap<String, String> args);

    boolean isAvailable();

    String getDescription();
}
