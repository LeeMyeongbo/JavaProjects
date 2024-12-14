package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.Pair;
import com.sec.bestreviewer.util.ResultStringFormatter;

import java.util.Collections;
import java.util.List;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;

public abstract class AbstractCommand implements Command {

    protected final OptionParser optionParser;
    protected final List<Pair<String, String>> conditionPairs;

    public AbstractCommand(OptionParser optionParser, List<Pair<String, String>> conditionPairs) {
        this.optionParser = optionParser;
        this.conditionPairs = conditionPairs;
    }

    @Override
    public List<String> execute(EmployeeStore employeeStore) {
        if (getCommandName().equals(CommandFactory.CMD_ADD)) {
            doExecute(employeeStore, Collections.emptyList());
            if (getCommandName().equals(CommandFactory.CMD_ADD)) {
                return Collections.emptyList();
            }
        }

        final List<Employee> employeeList = doExecute(employeeStore, getSearchedEmployees(employeeStore));
        if (optionParser.isPrintOn()) {
            return formatEmployeeList(employeeList, MAX_RESULT_NUMBER);
        }

        return formatEmployeeList(employeeList);
    }

    private List<Employee> getSearchedEmployees(EmployeeStore employeeStore) {
        final List<Employee> employeeList;

        if (!optionParser.isSecondOptionGroupExist()) {
            employeeList = employeeStore.search(conditionPairs.get(0).first, conditionPairs.get(0).second,
                    optionParser.getOptionGroup().secondOption(), optionParser.getOptionGroup().thirdOption());
        } else {
            Pair<String, String> firstParam = Pair.create(conditionPairs.get(0).first, conditionPairs.get(0).second);
            Pair<String, String> secondParam = Pair.create(conditionPairs.get(1).first, conditionPairs.get(1).second);

            if (optionParser.isAndCondition()) {
                employeeList = employeeStore.searchAnd(firstParam, secondParam, optionParser.getOptionGroup());
            } else if (optionParser.isOrCondition()) {
                employeeList = employeeStore.searchOr(firstParam, secondParam, optionParser.getOptionGroup());
            } else {
                employeeList = Collections.emptyList();
            }
        }
        return employeeList;
    }

    public Pair<String, String> getUpdateParams() {
        if (optionParser.isSecondOptionGroupExist()) {
            return conditionPairs.get(2);
        } else {
            return conditionPairs.get(1);
        }
    }

    protected abstract List<Employee> doExecute(EmployeeStore employeeStore, List<Employee> employeeList);

    protected List<String> formatEmployeeList(List<Employee> employeeList, int maxResults) {
        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, getCommandName(), maxResults);
    }

    protected List<String> formatEmployeeList(List<Employee> employeeList) {
        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, getCommandName());
    }

    protected abstract String getCommandName();
}
