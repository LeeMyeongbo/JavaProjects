package com.sec.bestreviewer.store;

import com.sec.bestreviewer.util.OptionGroup;
import com.sec.bestreviewer.util.Pair;

import java.util.List;

public interface EmployeeStore {

    String FIELD_EMPLOYEE_NUMBER = "employeeNumber";
    String FIELD_NAME = "name";
    String FIELD_CAREER_LEVEL = "careerLevel";
    String FIELD_PHONE_NUMBER = "phoneNumber";
    String FIELD_BIRTH_DAY = "birthDay";
    String FIELD_CERTI = "certi";

    List<Employee> search(String fieldName, String value, String secondOption, String thirdOption);

    List<Employee> delete(List<Employee> employeeList);

    List<Employee> modify(List<Employee> employeeList, Pair<String, String> updateParam);

    List<Employee> searchAnd(Pair<String, String> firstParam, Pair<String, String> secondParam,
                             OptionGroup optionGroup);

    List<Employee> searchOr(Pair<String, String> firstParam, Pair<String, String> secondParam,
                            OptionGroup optionGroup);

    void add(Employee employee);

    int count();
}
