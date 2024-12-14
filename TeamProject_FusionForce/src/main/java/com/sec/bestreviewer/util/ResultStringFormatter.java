package com.sec.bestreviewer.util;

import com.sec.bestreviewer.store.Employee;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.sec.bestreviewer.util.EmployeeUtils.getEmployeeNumber;

public class ResultStringFormatter {

    private ResultStringFormatter() {
    }

    public static List<String> getEmployeeListToFormattedString(List<Employee> employeeList,
             String commandType, int count) {
        if (employeeList.isEmpty()) {
            return Collections.singletonList(commandType + ",NONE");
        }
        return employeeList.stream()
                .sorted(Comparator.comparing(employee ->
                    getEmployeeNumber(Integer.parseInt(employee.getEmployeeNumber()))))
                .limit(count)
                .map(employee -> getEmployeeToFormattedString(commandType, employee))
                .collect(Collectors.toList());
    }

    public static String getEmployeeToFormattedString(String commandType, Employee employee) {
        return  commandType + "," + employee.getEmployeeNumber() + "," +
                employee.getName() + "," +
                employee.getCareerLevel() + "," +
                employee.getPhoneNumber() + "," +
                employee.getBirthday() + "," +
                employee.getCerti();
    }

    public static List<String> getEmployeeListToFormattedString(List<Employee> employeeList, String commandType) {
        final String formattedString =
                employeeList.isEmpty() ? "NONE" : Integer.toString(employeeList.size());

        return Collections.singletonList(commandType + "," + formattedString);
    }
}
