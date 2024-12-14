package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.Pair;

import java.util.List;

public class SearchCommand extends AbstractCommand {

    public SearchCommand(OptionParser optionParser, List<Pair<String, String>> conditionPairs) {
        super(optionParser, conditionPairs);
    }

    @Override
    protected List<Employee> doExecute(EmployeeStore employeeStore, List<Employee> employeeList) {
        return employeeList;
    }

    @Override
    protected String getCommandName() {
        return CommandFactory.CMD_SCH;
    }
}