package com.github.cb2222124.rental.models;

public interface Command {
    void execute();

    boolean isAvailable();

    String getDescription();
}
