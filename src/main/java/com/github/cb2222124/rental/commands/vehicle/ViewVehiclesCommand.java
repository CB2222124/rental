package com.github.cb2222124.rental.commands.vehicle;

import com.github.cb2222124.rental.Application;
import com.github.cb2222124.rental.models.Command;
import com.github.cb2222124.rental.models.Role;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Callan
 */
public class ViewVehiclesCommand implements Command {

    public ViewVehiclesCommandEmployee employeeCommand;
    public ViewVehiclesCommandCustomer customerCommand;

    public ViewVehiclesCommand() {
        this.employeeCommand = new ViewVehiclesCommandEmployee();
        this.customerCommand = new ViewVehiclesCommandCustomer();
    }

    @Override
    public void execute(LinkedHashMap<String, String> args) {
        if (Application.user.getRole() == Role.EMPLOYEE) {
            employeeCommand.execute(args);
        } else {
            customerCommand.execute(args);
        }
    }

    @Override
    public boolean isAvailable() {
        return Application.user.getRole() != Role.NONE;
    }

    @Override
    public String getDescription() {
        if (Application.user.getRole() == Role.EMPLOYEE) return employeeCommand.getDescription();
        return customerCommand.getDescription();
    }
}
