package com.github.cb2222124.rental.models;

import java.util.NoSuchElementException;

/**
 * Current application user information.
 *
 * @author Callan.
 */
public class User {

    private Role role;
    private int id;

    public User() {
        this.role = Role.NONE;
    }

    public void updateUser(Role role, int id) {
        this.role = role;
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public int getID() throws NoSuchElementException {
        if (role == Role.NONE) throw new NoSuchElementException("No ID applicable to user role NONE.");
        return id;
    }

    @Override
    public String toString() {
        return "User: {role=%s,id=%d}".formatted(role, id);
    }
}
