package com.sec.bestreviewer.store;

import com.sec.bestreviewer.CommandExecutor;
import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.command.Command;
import com.sec.bestreviewer.util.OptionType;
import com.sec.bestreviewer.util.ResultStringFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeStoreImplSecondOptionTest {

    private EmployeeStore store;
    private List<Employee> employeeList;

    @BeforeEach
    void setup() {
        employeeList = Arrays.asList(
                new Employee("100", "DKD LEE", "CL2", "010-9008-4833", "19940528", "PRO"),
                new Employee("101", "DOFE LEE", "CL4", "010-5498-4255", "19770530", "EX"),
                new Employee("103", "POEOFK KIM", "CL3", "010-4535-4538", "19940512", "PRO"),
                new Employee("102", "IIIE PARK", "CL2", "010-7822-4695", "19891123", "ADV"),
                new Employee("110", "DKD LEE", "CL3", "010-4485-1258", "19971118", "EX"),
                new Employee("200", "WOWKF GWARK", "CL2", "010-0027-7582", "19900101", "PRO"),
                new Employee("150", "ASDFDS PCKE", "CL3", "010-7250-7824", "19840308", "ADV"),
                new Employee("180", "LOKEF PIOE", "CL2", "010-0245-4254", "20000428", "PRO"),
                new Employee("190", "LOW EFE", "CL2", "010-8787-0000", "19940528", "ADV"),
                new Employee("800", "WOWKF GHIE", "CL2", "010-8787-0000", "19940512", "PRO"));
        store = new EmployeeStoreImpl();
        for (Employee employee : employeeList) {
            store.add(employee);
        }
    }

    @Test
    @DisplayName("두번째 옵션 FirstName 테스트")
    void testDeleteWithFirstNameOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.FIRST_NAME, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("name", "DKD");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_DEL, employeeList.get(0)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_DEL, employeeList.get(4)),
                resList.get(1));
    }

    @Test
    @DisplayName("두번째 옵션 LastName 테스트")
    void testSearchWithLastNameOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.LAST_NAME, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("name", "LEE");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(0)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(1)),
                resList.get(1));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(4)),
                resList.get(2));
    }

    @Test
    @DisplayName("두번째 옵션 중간 전화번호 테스트")
    void testSearchWithMiddlePhoneNumberOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.MIDDLE_NUMBER, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("phoneNum", "8787");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(9)),
                resList.get(1));
    }

    @Test
    @DisplayName("두번째 옵션 끝 전화번호 테스트")
    void testSearchWithLastPhoneNumberOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.LAST_NUMBER, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("phoneNum", "7582");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(5)),
                resList.get(0));
    }

    @Test
    @DisplayName("두번째 옵션 생일 연도 테스트")
    void testSearchWithBirthdayYearOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.YEAR, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("birthday", "1994");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(0)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(2)),
                resList.get(1));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(2));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(9)),
                resList.get(3));
    }

    @Test
    @DisplayName("두번째 옵션 생일 월 테스트")
    void testSearchWithBirthdayMonthOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.MONTH, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("birthday", "05");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(0)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(1)),
                resList.get(1));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(2)),
                resList.get(2));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(3));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(9)),
                resList.get(4));
    }

    @Test
    @DisplayName("두번째 옵션 생일 일 테스트")
    void testSearchWithBirthdayDayOption() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.DAY, OptionType.NO_OPTION);
        final List<String> params = Arrays.asList("birthday", "12");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(2)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(9)),
                resList.get(1));
    }
}
