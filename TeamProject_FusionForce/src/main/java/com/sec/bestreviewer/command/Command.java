package com.sec.bestreviewer.command;

import com.sec.bestreviewer.store.EmployeeStore;

import java.util.List;

public interface Command {

    List<String> execute(EmployeeStore employeeStore);
}
