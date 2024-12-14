package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;

import java.util.Collections;
import java.util.List;

public class AddCommand extends AbstractCommand {

    private final Employee employee;

    public AddCommand(OptionParser optionParser, Employee employee) {
        super(optionParser, Collections.emptyList());
        this.employee = employee;
    }

    @Override
    public List<Employee> doExecute(EmployeeStore employeeStore, List<Employee> employeeList) {
        employeeStore.add(employee);
        return Collections.emptyList();
    }

    @Override
    protected String getCommandName() {
        return CommandFactory.CMD_ADD;
    }
}
