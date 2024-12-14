package com.sec.bestreviewer;

import com.sec.bestreviewer.command.*;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {

    public static final String CMD_ADD = "ADD";
    public static final String CMD_DEL = "DEL";
    public static final String CMD_SCH = "SCH";
    public static final String CMD_CNT = "CNT";
    public static final String CMD_MOD = "MOD";

    private static final String EMPLOYEE_NUMBER = "employeeNum";
    private static final String NAME = "name";
    private static final String CAREER_LEVEL = "cl";
    private static final String PHONE_NUMBER = "phoneNum";
    private static final String BIRTHDAY = "birthday";
    private static final String CERTI = "certi";

    private static final Map<String, String> fieldMap = new HashMap<>();

    static {
        fieldMap.put(EMPLOYEE_NUMBER, EmployeeStore.FIELD_EMPLOYEE_NUMBER);
        fieldMap.put(NAME, EmployeeStore.FIELD_NAME);
        fieldMap.put(CAREER_LEVEL, EmployeeStore.FIELD_CAREER_LEVEL);
        fieldMap.put(PHONE_NUMBER, EmployeeStore.FIELD_PHONE_NUMBER);
        fieldMap.put(BIRTHDAY, EmployeeStore.FIELD_BIRTH_DAY);
        fieldMap.put(CERTI, EmployeeStore.FIELD_CERTI);
    }

    private CommandFactory() {
    }

    public static Command buildCommand(String cmd, List<String> options, List<String> params)
            throws IllegalArgumentException {
        final OptionParser optionParser = new OptionParser(options);

        return switch (cmd) {
            case CMD_ADD -> {
                final Employee employee = new Employee(params.get(0), params.get(1), params.get(2),
                    params.get(3), params.get(4), params.get(5));
                yield new AddCommand(optionParser, employee);
            }
            case CMD_DEL -> new DeleteCommand(optionParser, getConditionMapFromParams(params));
            case CMD_SCH -> new SearchCommand(optionParser, getConditionMapFromParams(params));
            case CMD_CNT -> new CountCommand();
            case CMD_MOD -> new ModifyCommand(optionParser, getConditionMapFromParams(params));
            default -> throw new IllegalArgumentException("Wrong command");
        };
    }

    private static List<Pair<String, String>> getConditionMapFromParams(List<String> params) {
        List<Pair<String, String>> conditionPairs = new ArrayList<>();
        for (int i = 0; i < params.size(); i += 2) {
            final String fieldName = fieldMap.get(params.get(i));
            if (fieldName == null) {
                throw new IllegalArgumentException("Wrong field: " + params.get(i));
            }
            conditionPairs.add(Pair.create(fieldName, params.get(i + 1)));
        }
        return conditionPairs;
     }
}
