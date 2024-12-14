package com.sec.bestreviewer;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeManagementTest {

    @Test
    public void testArgumentsEmptyInputFile() {
        String[] args = {
                "./src/test/java/com/sec/bestreviewer/emptyInput.txt",
                "./src/test/java/com/sec/bestreviewer/emptyOutput.txt"
        };

        EmployeeManagement employeeManagement = new EmployeeManagement();
        employeeManagement.run(args);

        File file = new File("./src/test/java/com/sec/bestreviewer/emptyOutput.txt");

        assertTrue(file.exists());
    }

    @Test
    public void testArgumentsWrongArgsCount() {
        String[] args = {"input.txt"};

        EmployeeManagement employeeManagement = new EmployeeManagement();

        Assertions.assertThrows(IllegalArgumentException.class, () -> employeeManagement.run(args));
    }

    @Test
    public void testArgumentsNotExistInputFile() {
        String[] args = {"notExist.txt", "emptyOutput.txt"};

        EmployeeManagement employeeManagement = new EmployeeManagement();

        Assertions.assertThrows(IllegalArgumentException.class, () -> employeeManagement.run(args));
    }

    @Test
    public void integrationTest() {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/integration_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/integration_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        employeeManagement.run(args);
        Approvals.verify(outputFile);
    }
}
