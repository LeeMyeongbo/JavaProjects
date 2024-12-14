package com.sec.bestreviewer.store;

public class Injection {

    public static EmployeeStore provideEmployeeStore() {
        return new EmployeeStoreImpl();
    }
}
