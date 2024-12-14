package com.sec.bestreviewer.util;

import com.sec.bestreviewer.store.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeUtils {

    private static final int YEAR_1990_EMPLOYEE_NUMBER = 90_000000;
    private static final int YEAR_PREFIX_19 = 1900_000000;
    private static final int YEAR_PREFIX_20 = 2000_000000;

    private EmployeeUtils() {
    }

    public static Integer getEmployeeNumber(int employeeNumber) {
        return employeeNumber >= YEAR_1990_EMPLOYEE_NUMBER ?
                YEAR_PREFIX_19 + employeeNumber : YEAR_PREFIX_20 + employeeNumber;
    }

    public static List<Employee> findCommonEmployees(List<Employee> searchedEmployeesFirst, List<Employee> searchedEmployeesSecond) {
        List<Employee> commonEmployees = new ArrayList<>();
        for (Employee empFirst : searchedEmployeesFirst) {
            for (Employee empSecond : searchedEmployeesSecond) {
                if (empFirst.getEmployeeNumber().equals(empSecond.getEmployeeNumber())) {
                    commonEmployees.add(empFirst);
                    break;
                }
            }
        }
        return commonEmployees;
    }

    public static List<Employee> mergeEmployeesUsingMap(List<Employee> searchedEmployeesFirst, List<Employee> searchedEmployeesSecond) {
        Map<String, Employee> commonEmployeesMap = new HashMap<>();
        for (Employee empFirst : searchedEmployeesFirst) {
            commonEmployeesMap.put(empFirst.getEmployeeNumber(), empFirst);
        }
        for (Employee empSecond : searchedEmployeesSecond) {
            commonEmployeesMap.put(empSecond.getEmployeeNumber(), empSecond);
        }
        return new ArrayList<>(commonEmployeesMap.values());
    }

    public static boolean isValidThirdOption(String thirdOption) {
        return thirdOption.equals(OptionType.GREATER_EQUAL)
                || thirdOption.equals(OptionType.GREATER)
                || thirdOption.equals(OptionType.SMALLER_EQUAL)
                || thirdOption.equals(OptionType.SMALLER);
    }
}
