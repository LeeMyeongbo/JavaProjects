package com.sec.bestreviewer;

import com.sec.bestreviewer.command.Command;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionGroup;
import com.sec.bestreviewer.util.Pair;
import com.sec.bestreviewer.util.ResultStringFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;
import static com.sec.bestreviewer.util.OptionType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandExecutorTest {

    private EmployeeStore employeeStore;

    @BeforeEach
    void createMockEmployeeStore() {
        employeeStore = mock(EmployeeStore.class);
    }

    @Test
    void queryExecutorReturnsResultString() {
        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("18064527", "ANDY KIM", "CL2", "010-9623-6213", "19890803", "PRO");
        final Command command = CommandFactory.buildCommand("ADD", options, params);
        final List<String> res = (new CommandExecutor()).execute(command);
        assertNotNull(res);
    }

    @Test
    void testAddCommandReturnsEmptyList() {
        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("18064527", "ANDY KIM", "CL2", "010-9623-6213", "19890803", "PRO");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_ADD, options, params);
        final List<String> res = (new CommandExecutor()).execute(command);
        assertNotNull(res);
        assertEquals(0, res.size());
    }

    @Test
    void testDeleteCommandWithPrintOption() {
        deleteCommandWithPrintOption(1);
        deleteCommandWithPrintOption(6);
    }

    private void deleteCommandWithPrintOption(int count) {
        final List<Employee> employeeList = getEmployees(count);
        when(employeeStore.delete(anyList())).thenReturn(employeeList);

        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        for (int i = 0; i < Math.min(count, MAX_RESULT_NUMBER); i++) {
            assertEquals(ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_DEL, employeeList.get(i)),
                    resList.get(i));
        }
    }

    private List<Employee> getEmployees(int count) {
        final List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String employeeNumber = Integer.toString(90_000000 + (i * 10_000000));
            employeeList.add(
                    new Employee(employeeNumber, "SEO KFI", "CL1", "010-1234-5678", "20190101", "PRO"));
        }
        return employeeList;
    }

    @Test
    void testDeleteCommandWithPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        when(employeeStore.delete(anyList())).thenReturn(employeeList);

        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_DEL + ",NONE", resList.get(0));
    }

    @Test
    void testDeleteCommandWithOutPrintOption() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        when(employeeStore.delete(anyList())).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_DEL + "," + deletedCount, resList.get(0));
    }

    @Test
    void testDeleteCommandWithOutPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        when(employeeStore.delete(anyList())).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_DEL + ",NONE", resList.get(0));
    }

    @Test
    void testDeleteCommandWithAndOption() {
        final List<Employee> employeeList = Collections.emptyList();
        when(employeeStore.delete(anyList())).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION, AND, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM", "name", "SEO KFI");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_DEL + ",NONE", resList.get(0));
    }

    @Test
    void testDeleteCommandWithOrOption() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        when(employeeStore.delete(anyList())).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION, OR, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM", "name", "SEO KFI");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_DEL + "," + deletedCount, resList.get(0));
    }

    @Test
    void testSearchCommandWithPrintOption() {
        searchCommandWithPrintOption(1);
        searchCommandWithPrintOption(6);
    }

    void searchCommandWithPrintOption(int count) {
        final List<Employee> employeeList = getEmployees(count);
        when(employeeStore.search("name", "ANDY KIM", NO_OPTION, NO_OPTION))
                .thenReturn(employeeList);

        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        for (int i = 0; i < Math.min(count, MAX_RESULT_NUMBER); i++) {
            assertEquals(
                    ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(i)),
                    resList.get(i));
        }
    }

    @Test
    void testSearchCommandWithPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        when(employeeStore.search("name", "ANDY KIM", NO_OPTION, NO_OPTION))
                .thenReturn(employeeList);

        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_SCH + ",NONE", resList.get(0));
    }

    @Test
    void testSearchCommandWithOutPrintOption() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        when(employeeStore.search("name", "ANDY KIM", NO_OPTION, NO_OPTION))
                .thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_SCH + "," + deletedCount, resList.get(0));
    }

    @Test
    void testSearchCommandWithOutPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        when(employeeStore.search("name", "ANDY KIM", NO_OPTION, NO_OPTION))
                .thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_SCH + ",NONE", resList.get(0));
    }


    @Test
    void testSearchCerti() {
        final List<Employee> employeeList = getEmployees(5);
        when(employeeStore.search("certi", "PRO", NO_OPTION, NO_OPTION))
                .thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("certi", "PRO");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_SCH + ",5", resList.get(0));
    }

    @Test
    void testSearchWithAndOption() {
        List<Employee> employeeList = getEmployees(5);
        employeeList.add(new Employee("18064527", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));
        employeeList.add(new Employee("18064529", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));

        when(employeeStore.searchAnd(any(Pair.class), any(Pair.class),any(OptionGroup.class))).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION
                , AND, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("certi", "PRO", "cl", "CL1");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_SCH + ",7", resList.get(0));
    }

    @Test
    void testSearchWithOrOption() {
        List<Employee> employeeList = getEmployees(5);
        employeeList.add(new Employee("18064527", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));
        employeeList.add(new Employee("18064529", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));

        when(employeeStore.searchOr(any(Pair.class), any(Pair.class),any(OptionGroup.class))).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION
                , OR, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "SEO KFI", "name", "YUJIN KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_SCH + ",7", resList.get(0));
    }


    @Test
    void testCountCommandShouldReturnCountNumberString() {
        when(employeeStore.count()).thenReturn(1);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Collections.emptyList();
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_CNT, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(CommandFactory.CMD_CNT + ",1", resList.get(0));
    }

    @Test
    void testDeleteCommandReturnsSortedEmployeeList() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        final List<Employee> reversedEmployeeList = employeeList.stream()
                .sorted(Comparator.comparing(Employee::getEmployeeNumber).reversed())
                .collect(Collectors.toList());
        when(employeeStore.delete(anyList())).thenReturn(reversedEmployeeList);

        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        for (int i = 0; i < Math.min(deletedCount, MAX_RESULT_NUMBER); i++) {
            assertEquals(
                    ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_DEL, employeeList.get(i)),
                    resList.get(i));
        }
    }

    @Test
    void testModifyCommandWithPrintOption() {
        final List<Employee> employeeListBeforeMOD = Arrays.asList(new Employee("18064527", "YUJIN KIM", "CL2", "010-9623-6213", "19890803", "PRO"));
        final List<Employee> employeeListForMOD = Arrays.asList(new Employee("18064527", "YUJIN KIM", "CL2", "010-9623-6213", "19890803", "PRO"));
        when(employeeStore.modify(anyList(), any(Pair.class))).thenReturn(employeeListForMOD);
        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "YUJIN KIM", "name", "YUJIN LEE");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_MOD, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(1, resList.size());
        assertEquals(ResultStringFormatter.getEmployeeToFormattedString("MOD", employeeListBeforeMOD.get(0)), resList.get(0));
    }

    @Test
    void testModifyCommandWithOutPrintOption() {
        final List<Employee> employeeList = Arrays.asList(new Employee("18064527", "YUJIN KIM", "CL2", "010-9623-6213", "19890803", "PRO"));
        when(employeeStore.modify(anyList(), any(Pair.class))).thenReturn(employeeList);
        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "YUJIN KIM", "name", "YUJIN LEE");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_MOD, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(1, resList.size());
        assertEquals("MOD,1", resList.get(0));
    }

    @Test
    void testModifyCommandWithNoResult() {
        final List<Employee> employeeList = Collections.emptyList();
        when(employeeStore.search("name", "YUJIN KIM", NO_OPTION, NO_OPTION))
                .thenReturn(employeeList);
        final List<String> options = Arrays.asList("-p", NO_OPTION, NO_OPTION);
        final List<String> params = Arrays.asList("name", "YUJIN KIM", "name", "YUJIN Lee");
        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_MOD, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);
        assertEquals(1, resList.size());
        assertEquals("MOD,NONE", resList.get(0));
    }

    @Test
    void testModifyAndOption() {
        List<Employee> employeeList = getEmployees(5);
        employeeList.add(new Employee("18064527", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));
        employeeList.add(new Employee("18064527", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));

        final List<String> params = Arrays.asList("certi", "PRO", "cl", "CL1", "cl", "CL2");

        when(employeeStore.modify(anyList(), any(Pair.class))).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION, AND, NO_OPTION, NO_OPTION);

        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_MOD, options, params);
        List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        assertEquals(7, employeeList.size());
        assertEquals("MOD,7", resList.get(0));
    }

    @Test
    void testModifyOrOption() {
        List<Employee> employeeList = getEmployees(5);
        employeeList.add(new Employee("18064527", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));
        employeeList.add(new Employee("18064529", "YUJIN KIM", "CL1", "010-9623-6213", "19890803", "PRO"));

        final List<String> params = Arrays.asList("name", "YUJIN KIM", "name", "SEO KFI", "cl", "CL2");

        when(employeeStore.modify(anyList(), any(Pair.class))).thenReturn(employeeList);

        final List<String> options = Arrays.asList(NO_OPTION, NO_OPTION, NO_OPTION, OR, NO_OPTION, NO_OPTION);

        final Command command = CommandFactory.buildCommand(CommandFactory.CMD_MOD, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        assertEquals(7, employeeList.size());
        assertEquals("MOD,7", resList.get(0));
    }
}