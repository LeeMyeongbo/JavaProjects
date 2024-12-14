package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.EmployeeStore;

import java.util.Collections;
import java.util.List;

public class CountCommand implements Command {

    @Override
    public List<String> execute(EmployeeStore employeeStore) {
        return Collections.singletonList(CommandFactory.CMD_CNT + "," + employeeStore.count());
    }
}
