package com.github.cb2222124.rental.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Interface used to specify the behavior commands must implement.
 *
 * @author Callan.
 */
public interface Command {
    void execute(LinkedHashMap<String, String> args);

    boolean isAvailable();

    String getDescription();
}
