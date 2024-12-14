package com.sec.bestreviewer.testutil;

import com.sec.bestreviewer.store.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {

    private TestUtil() {
    }

    /**
     * Verifies that the given list of employee numbers matches the employee numbers in the given list of employees.<br>
     * <br>
     * Example Usage :<br>
     * <pre>{@code
     * verify(new String[]{"100", "110"}, store.search(EmployeeStore.FIELD_NAME, "DKD LEE", ""));
     * }
     * </pre>
     *
     * @param expectedList the expected list of employee number
     * @param searchedList the searched list of employee to be verified
     */
    public static void verify(String[] expectedList, List<Employee> searchedList) {
        for (int i = 0; i < expectedList.length; i++) {
            assertEquals(expectedList[i], searchedList.get(i).getEmployeeNumber());
        }
        if (expectedList.length == 0) {
            assertTrue(searchedList.isEmpty());
        }
    }
}
