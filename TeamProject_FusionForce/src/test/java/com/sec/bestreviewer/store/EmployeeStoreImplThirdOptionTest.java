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

public class EmployeeStoreImplThirdOptionTest {

    private EmployeeStore store;
    private List<Employee> employeeList;

    @BeforeEach
    void setup() {
        employeeList = Arrays.asList(
                new Employee("19009991", "DKD LEE", "CL2", "010-9008-4833", "19940528", "PRO"),
                new Employee("19009992", "DOFE LEE", "CL4", "010-5498-4255", "19770530", "EX"),
                new Employee("19009993", "POEOFK KIM", "CL3", "010-4535-4538", "19940512", "PRO"),
                new Employee("19009994", "IIIE PARK", "CL2", "010-7822-4695", "19891123", "ADV"),
                new Employee("20002700", "DKD LEE", "CL3", "010-4485-1258", "19971118", "EX"),
                new Employee("20002701", "WOWKF GWARK", "CL2", "010-0027-7582", "19900101", "PRO"),
                new Employee("20002702", "ASDFDS PCKE", "CL3", "010-7250-7824", "19840308", "ADV"),
                new Employee("95009998", "LOKEF PIOE", "CL2", "010-0245-4254", "20000428", "PRO"),
                new Employee("95009999", "LOW EFE", "CL2", "010-8787-0000", "19940528", "ADV"),
                new Employee("95060000", "WOWKF GHIE", "CL2", "010-8787-0000", "19940512", "ADV"));
        store = new EmployeeStoreImpl();
        for (Employee employee : employeeList) {
            store.add(employee);
        }
    }

    @Test
    @DisplayName("사번이 param 보다 높은 직원을 검색한다")
    void testCompareSearchWithEmployeeNumber() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.NO_OPTION, OptionType.GREATER);
        final List<String> params = Arrays.asList("employeeNum", "19009993");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(3)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(4)),
                resList.get(1));
    }

    @Test
    @DisplayName("이름의 알파벳 순서가 param 보다 같거나 높은 직원을 검색한다")
    void testCompareSearchWithName() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.NO_OPTION, OptionType.GREATER_EQUAL);
        final List<String> params = Arrays.asList("name", "POEOFK KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(9)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(2)),
                resList.get(1));
    }

    @Test
    @DisplayName("CAREER_LEVEL의 알파벳 순서가 param 보다 낮은 직원을 검색한다")
    void testCompareSearchWithCl() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.NO_OPTION, OptionType.SMALLER_EQUAL);
        final List<String> params = Arrays.asList("cl", "CL3");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(1));
    }

    @Test
    @DisplayName("전화번호가 param 보다 같거나 낮은 직원을 검색한다")
    void testCompareSearchWithPhoneNum() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.NO_OPTION, OptionType.SMALLER_EQUAL);
        final List<String> params = Arrays.asList("phoneNum", "010-7822-4695");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(1)),
                resList.get(1));
    }

    @Test
    @DisplayName("생일이 param과 같거나 높은 직원을 검색한다")
    void testCompareSearchWithBirthday() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.NO_OPTION, OptionType.GREATER_EQUAL);
        final List<String> params = Arrays.asList("birthday", "19940528");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(1));
    }

    @Test
    @DisplayName("Certi의 레벨이 param 보다 같거나 낮은 직원을 검색한다")
    void testCompareSearchWithCerti() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.NO_OPTION, OptionType.SMALLER_EQUAL);
        final List<String> params = Arrays.asList("certi", "PRO");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(1));
    }

    @Test
    @DisplayName("두 번째 옵션과 같이 테스트 - first name 비교 검색")
    void testCompareSearchWithFirstName() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.FIRST_NAME, OptionType.GREATER_EQUAL);
        final List<String> params = Arrays.asList("name", "LOW");
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
    @DisplayName("두 번째 옵션과 같이 테스트 - last name 비교 검색")
    void testCompareSearchWithLastName() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.LAST_NAME, OptionType.GREATER);
        final List<String> params = Arrays.asList("name", "LEE");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(3)),
                resList.get(1));
    }

    @Test
    @DisplayName("두 번째 옵션과 같이 테스트 - 전화번호 앞자리 비교 검색")
    void testCompareSearchWithMiddleNumber() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.MIDDLE_NUMBER, OptionType.SMALLER);
        final List<String> params = Arrays.asList("phoneNum", "4535");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(4)),
                resList.get(1));
    }

    @Test
    @DisplayName("두 번째 옵션과 같이 테스트 - 전화번호 뒷자리 비교 검색")
    void testCompareSearchWithLastNumber() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.LAST_NUMBER, OptionType.SMALLER_EQUAL);
        final List<String> params = Arrays.asList("phoneNum", "4255");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(8)),
                resList.get(1));
    }

    @Test
    @DisplayName("두 번째 옵션과 같이 테스트 - 생년 비교 검색")
    void testCompareSearchWithBirthYear() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.YEAR, OptionType.GREATER);
        final List<String> params = Arrays.asList("birthday", "1994");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(4)),
                resList.get(1));
    }

    @Test
    @DisplayName("두 번째 옵션과 같이 테스트 - 생월 비교 검색")
    void testCompareSearchWithBirthMonth() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.MONTH, OptionType.SMALLER);
        final List<String> params = Arrays.asList("birthday", "05");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(7)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(5)),
                resList.get(1));
    }

    @Test
    @DisplayName("두 번째 옵션과 같이 테스트 - 생일 비교 검색")
    void testCompareSearchWithBirthDay() {
        final List<String> options = Arrays.asList(OptionType.PRINT, OptionType.DAY, OptionType.SMALLER_EQUAL);
        final List<String> params = Arrays.asList("birthday", "12");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(store)).execute(command);
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(9)),
                resList.get(0));
        assertEquals(
                ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(2)),
                resList.get(1));
    }
}
