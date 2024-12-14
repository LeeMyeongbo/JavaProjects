package com.sec.bestreviewer;

import com.sec.bestreviewer.command.Command;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.store.Injection;
import com.sec.bestreviewer.util.CachedSupplier;

import java.util.List;

public class CommandExecutor {

    public static final int MAX_RESULT_NUMBER = 5;

    private final CachedSupplier<EmployeeStore> employeeStore;

    CommandExecutor() {
        employeeStore = new CachedSupplier<>(Injection::provideEmployeeStore);
    }

    public CommandExecutor(EmployeeStore employeeStore) {
        this.employeeStore = new CachedSupplier<>(() -> employeeStore);
    }

    public List<String> execute(Command command) {
        return command.execute(employeeStore.get());
    }
}