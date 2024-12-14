package com.sec.bestreviewer.util;

import static com.sec.bestreviewer.util.EmployeeUtils.getEmployeeNumber;

public class ValueCompareUtil {

    private ValueCompareUtil() {
    }

    public static boolean compareString(String fieldValue, String value, String option) {
        return switch (option) {
            case OptionType.GREATER_EQUAL -> fieldValue.compareTo(value) >= 0;
            case OptionType.GREATER -> fieldValue.compareTo(value) > 0;
            case OptionType.SMALLER -> fieldValue.compareTo(value) < 0;
            case OptionType.SMALLER_EQUAL -> fieldValue.compareTo(value) <= 0;
            default -> false;
        };
    }

    public static boolean compareEmployeeNumber(String employeeNumber, String value, String option) {
        int numEmployee = getEmployeeNumber(Integer.parseInt(employeeNumber));
        int numValue = getEmployeeNumber(Integer.parseInt(value));
        return compareString(Integer.toString(numEmployee), Integer.toString(numValue), option);
    }

    public static boolean compareCertiLevel(String employeeCerti, String value, String option) {
        int numEmployee = getCertiNumber(employeeCerti);
        int numValue = getCertiNumber(value);
        return compareString(Integer.toString(numEmployee), Integer.toString(numValue), option);
    }

    public static int getCertiNumber(String certi) {
        return switch (certi) {
            case "ADV" -> 1;
            case "PRO" -> 2;
            case "EX" -> 3;
            default -> 0;
        };
    }
}
