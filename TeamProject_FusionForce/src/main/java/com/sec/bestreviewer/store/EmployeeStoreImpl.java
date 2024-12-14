package com.sec.bestreviewer.store;

import com.sec.bestreviewer.util.OptionGroup;
import com.sec.bestreviewer.util.Pair;
import com.sec.bestreviewer.util.ValueCompareUtil;

import java.util.*;
import java.util.stream.Collectors;

import static com.sec.bestreviewer.util.EmployeeUtils.*;

public class EmployeeStoreImpl implements EmployeeStore {

    Map<String, Employee> employees = new TreeMap<>(
            Comparator.comparingInt(employeeNum -> getEmployeeNumber(Integer.parseInt(employeeNum))));

    @Override
    public List<Employee> search(String fieldName, String value, String secondOption, String thirdOption) {
        return switch (fieldName) {
            case EmployeeStore.FIELD_EMPLOYEE_NUMBER -> employees.entrySet().parallelStream()
                .filter(employee -> !isValidThirdOption(thirdOption)
                    ? employee.getValue().getEmployeeNumber().equals(value)
                    : ValueCompareUtil.compareEmployeeNumber(employee.getValue().getEmployeeNumber(), value, thirdOption))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
            case EmployeeStore.FIELD_NAME -> employees.entrySet().parallelStream()
                .filter(employee -> !isValidThirdOption(thirdOption)
                    ? employee.getValue().getName(secondOption).equals(value)
                    : ValueCompareUtil.compareString(employee.getValue().getName(secondOption), value, thirdOption))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
            case EmployeeStore.FIELD_CAREER_LEVEL -> employees.entrySet().parallelStream()
                .filter(employee -> !isValidThirdOption(thirdOption)
                    ? employee.getValue().getCareerLevel().equals(value)
                    : ValueCompareUtil.compareString(employee.getValue().getCareerLevel(), value, thirdOption))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
            case EmployeeStore.FIELD_PHONE_NUMBER -> employees.entrySet().parallelStream()
                .filter(employee -> !isValidThirdOption(thirdOption)
                    ? employee.getValue().getPhoneNumber(secondOption).equals(value)
                    : ValueCompareUtil.compareString(employee.getValue().getPhoneNumber(secondOption), value, thirdOption))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
            case EmployeeStore.FIELD_BIRTH_DAY -> employees.entrySet().parallelStream()
                .filter(employee -> !isValidThirdOption(thirdOption)
                    ? employee.getValue().getBirthday(secondOption).equals(value)
                    : ValueCompareUtil.compareString(employee.getValue().getBirthday(secondOption), value, thirdOption))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
            case EmployeeStore.FIELD_CERTI -> employees.entrySet().parallelStream()
                .filter(employee -> !isValidThirdOption(thirdOption)
                    ? employee.getValue().getCerti().equals(value)
                    : ValueCompareUtil.compareCertiLevel(employee.getValue().getCerti(), value, thirdOption))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
            default -> Collections.emptyList();
        };
    }

    @Override
    public List<Employee> delete(List<Employee> employeeList) {
        for (Employee employee: employeeList) {
            employees.remove(employee.getEmployeeNumber());
        }
        return employeeList;
    }

    @Override
    public List<Employee> searchAnd(Pair<String, String> firstParam,
                                    Pair<String, String> secondParam, OptionGroup optionGroup) {
        List<Employee> searchedEmployeesFirst = search(firstParam.first, firstParam.second,
                                                       optionGroup.secondOption(), optionGroup.thirdOption());
        List<Employee> searchedEmployeesSecond = search(secondParam.first, secondParam.second,
                                                        optionGroup.secondOption2(), optionGroup.thirdOption2());

        return findCommonEmployees(searchedEmployeesFirst, searchedEmployeesSecond);
    }

    @Override
    public List<Employee> searchOr(Pair<String, String> firstParam,
                                   Pair<String, String> secondParam, OptionGroup optionGroup) {
        List<Employee> searchedEmployeesFirst = search(firstParam.first, firstParam.second,
                optionGroup.secondOption(), optionGroup.thirdOption());
        List<Employee> searchedEmployeesSecond = search(secondParam.first, secondParam.second,
                optionGroup.secondOption2(), optionGroup.thirdOption2());

        return mergeEmployeesUsingMap(searchedEmployeesFirst, searchedEmployeesSecond);
    }

    @Override
    public void add(Employee employee) {
        employees.put(employee.getEmployeeNumber(), employee);
    }

    @Override
    public int count() {
        return employees.size();
    }

    @Override
    public List<Employee> modify(List<Employee> employeeList, Pair<String, String> updateParam) {
        List<Employee> originalRecords = createOriginalRecords(employeeList);

        for (Employee employee : employeeList) {
            updateEmployeeField(employee, updateParam.first, updateParam.second);
        }
        return originalRecords;
    }

    private List<Employee> createOriginalRecords(List<Employee> employees) {
        List<Employee> originalRecords = new ArrayList<>();
        for (Employee employee : employees) {
            originalRecords.add(new Employee(employee.getEmployeeNumber(),
                    employee.getName(),
                    employee.getCareerLevel(),
                    employee.getPhoneNumber(),
                    employee.getBirthday(),
                    employee.getCerti()));
        }
        return originalRecords;
    }

    private void updateEmployeeField(Employee employee, String updateFieldName, String updateValue) {
        switch (updateFieldName) {
            case EmployeeStore.FIELD_NAME -> employee.setName(updateValue);
            case EmployeeStore.FIELD_CAREER_LEVEL -> employee.setCareerLevel(updateValue);
            case EmployeeStore.FIELD_PHONE_NUMBER -> employee.setPhoneNumber(updateValue);
            case EmployeeStore.FIELD_BIRTH_DAY -> employee.setBirthday(updateValue);
            case EmployeeStore.FIELD_CERTI -> employee.setCerti(updateValue);
            default -> throw new IllegalArgumentException("Invalid field: " + updateFieldName);
        }
    }
}
