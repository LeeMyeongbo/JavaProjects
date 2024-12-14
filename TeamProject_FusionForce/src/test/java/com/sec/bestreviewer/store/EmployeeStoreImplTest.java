package com.sec.bestreviewer.store;

import com.sec.bestreviewer.util.OptionGroup;
import com.sec.bestreviewer.util.OptionType;
import com.sec.bestreviewer.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.sec.bestreviewer.testutil.TestUtil.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeStoreImplTest {

    private final OptionGroup group = new OptionGroup(OptionType.NO_OPTION, OptionType.NO_OPTION,
            OptionType.NO_OPTION, OptionType.NO_OPTION);
    private EmployeeStore store;

    @BeforeEach
    void setup() {
        List<Employee> employeeList = Arrays.asList(
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
    @DisplayName("EmployeeStoreImpl 내 add 확인")
    void testAdd() {
        assertEquals(10, store.count());
    }

    @ParameterizedTest
    @CsvSource({
            "100, DKD LEE, CL2, 010-9008-4833, 19940528, PRO",
            "101, DOFE LEE, CL4, 010-5498-4255, 19770530, EX",
            "103, POEOFK KIM, CL3, 010-4535-4538, 19940512, PRO",
            "102, IIIE PARK, CL2, 010-7822-4695, 19891123, ADV",
            "150, ASDFDS PCKE, CL3, 010-7250-7824, 19840308, ADV",
            "180, LOKEF PIOE, CL2, 010-0245-4254, 20000428, PRO",
            "200, WOWKF GWARK, CL2, 010-0027-7582, 19900101, PRO"
    })
    @DisplayName("EmployeeStoreImpl 내 Employee Number 통한 search 확인")
    void testSearchWithEmployeeNum(
            String employeeNum, String name, String level, String phoneNum, String birthday, String cert
    ) {
        List<Employee> searchedList = store.search(EmployeeStore.FIELD_EMPLOYEE_NUMBER, employeeNum,
                                                   OptionType.NO_OPTION, OptionType.NO_OPTION);
        assertEquals(employeeNum, searchedList.get(0).getEmployeeNumber());
        assertEquals(name, searchedList.get(0).getName());
        assertEquals(level, searchedList.get(0).getCareerLevel());
        assertEquals(phoneNum, searchedList.get(0).getPhoneNumber());
        assertEquals(birthday, searchedList.get(0).getBirthday());
        assertEquals(cert, searchedList.get(0).getCerti());
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 Employee Name 통한 search 확인")
    void testSearchWithEmployeeName() {
        verify(new String[]{"100", "110"}, store.search(EmployeeStore.FIELD_NAME, "DKD LEE",
                                                        OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"180"}, store.search(EmployeeStore.FIELD_NAME, "LOKEF PIOE",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[0], store.search(EmployeeStore.FIELD_NAME, "XXX XXX",
                                           OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 Employee career level 통한 search 확인")
    void testSearchWithEmployeeLevel() {
        verify(new String[]{"100", "102", "180", "190", "200", "800"},
                store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL2", OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"103", "110", "150"}, store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL3",
                                                               OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"101"}, store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL4",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 Employee birthday 통한 search 확인")
    void testSearchWithEmployeeBirthDay() {
        verify(new String[]{"101"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19770530",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"200"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19900101",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "190"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19940528",
                                                        OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"103", "800"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19940512",
                                                        OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 Employee phone number 통한 search 확인")
    void testSearchWithEmployeePhoneNum() {
        verify(new String[]{"200"}, store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-0027-7582",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"103"}, store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-4535-4538",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"190", "800"}, store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[0], store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-5555-6000",
                                                 OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 Employee certi 통한 search 확인")
    void testSearchWithCerti() {
        verify(new String[]{"102", "150", "190"}, store.search(EmployeeStore.FIELD_CERTI, "ADV",
                                                               OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "103", "180", "200", "800"}, store.search(EmployeeStore.FIELD_CERTI, "PRO",
                                                                             OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"101", "110"}, store.search(EmployeeStore.FIELD_CERTI, "EX",
                                                        OptionType.NO_OPTION,OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("column이 존재하지 않을 때 search 확인")
    void testSearchWhenNoColumnExisted() {
        verify(new String[0], store.search("Ghost", "00", OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl delete 확인")
    void testDelete() {
        verify(new String[]{"100"}, store.delete(
                store.search(EmployeeStore.FIELD_EMPLOYEE_NUMBER, "100", OptionType.NO_OPTION, OptionType.NO_OPTION)));
        assertEquals(9, store.count());

        verify(new String[]{"150"}, store.delete(
                store.search(EmployeeStore.FIELD_BIRTH_DAY, "19840308", OptionType.NO_OPTION, OptionType.NO_OPTION)));
        assertEquals(8, store.count());

        verify(new String[]{"190", "800"}, store.delete(
                store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000",
                OptionType.NO_OPTION, OptionType.NO_OPTION)));
        assertEquals(6, store.count());

        verify(new String[]{"101", "110"}, store.delete(
                store.search(EmployeeStore.FIELD_CERTI, "EX", OptionType.NO_OPTION, OptionType.NO_OPTION)));
        assertEquals(4, store.count());

        verify(new String[0], store.delete(
                store.search(EmployeeStore.FIELD_NAME, "DKD LEE", OptionType.NO_OPTION, OptionType.NO_OPTION)));
        assertEquals(4, store.count());

        verify(new String[0], store.delete(
                store.search(EmployeeStore.FIELD_NAME, "JJJ LLL", OptionType.NO_OPTION, OptionType.NO_OPTION)));
        assertEquals(4, store.count());
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 name modify 확인")
    void testModifyName() {
        verify(new String[]{"100", "110"}, store.modify(
                store.search(EmployeeStore.FIELD_NAME, "DKD LEE", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_NAME, "HKH KIM")));
        verify(new String[0], store.search(EmployeeStore.FIELD_NAME, "DKD LEE",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "110"}, store.search(EmployeeStore.FIELD_NAME, "HKH KIM",
                OptionType.NO_OPTION, OptionType.NO_OPTION));

        store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL3",
                OptionType.NO_OPTION, OptionType.NO_OPTION);
        verify(new String[]{"103", "110", "150"}, store.modify(store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL3",
                        OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_NAME, "AAA BBBB")));
        verify(new String[0], store.search(EmployeeStore.FIELD_NAME, "POEOFK KIM",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100"}, store.search(EmployeeStore.FIELD_NAME, "HKH KIM",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[0], store.search(EmployeeStore.FIELD_NAME, "ASDFDS PCKE",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"103", "110", "150"}, store.search(EmployeeStore.FIELD_NAME, "AAA BBBB",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 career level modify 확인")
    void testModifyCareerLevel() {
        verify(new String[]{"100", "102", "180", "190", "200", "800"},
                store.modify(store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL2", OptionType.NO_OPTION, OptionType.NO_OPTION),
                             Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL3")));
        verify(new String[0], store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL2",
                                           OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "102", "103", "110", "150", "180", "190", "200", "800"},
                store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL3",
                             OptionType.NO_OPTION, OptionType.NO_OPTION));

        verify(new String[]{"190", "800"}, store.modify(
                store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL2")));
        verify(new String[]{"190", "800"}, store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL2",
                                                        OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "102", "103", "110", "150", "180", "200"},
                store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL3",
                             OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 phone number modify 확인")
    void testModifyPhoneNumber() {
        verify(new String[]{"103"}, store.modify(
                store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-4535-4538", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000")));
        verify(new String[0], store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-4535-4538",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"103", "190", "800"}, store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000",
                OptionType.NO_OPTION, OptionType.NO_OPTION));

        verify(new String[]{"100", "190"}, store.modify(
                store.search(EmployeeStore.FIELD_BIRTH_DAY, "19940528", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-6698-7700")));
        verify(new String[0], store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-9008-4833",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"103", "800"}, store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000",
                OptionType.NO_OPTION ,OptionType.NO_OPTION));
        verify(new String[]{"100", "190"}, store.search(EmployeeStore.FIELD_PHONE_NUMBER, "010-6698-7700",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 birthday modify 확인")
    void testModifyBirthday() {
        verify(new String[]{"110"}, store.modify(
                store.search(EmployeeStore.FIELD_BIRTH_DAY, "19971118", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "19971117")));
        verify(new String[0], store.search(EmployeeStore.FIELD_BIRTH_DAY, "19971118",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"110"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19971117",
                OptionType.NO_OPTION, OptionType.NO_OPTION));

        verify(new String[]{"102", "150", "190"}, store.modify(
                store.search(EmployeeStore.FIELD_CERTI, "ADV", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "19770530")));
        verify(new String[0], store.search(EmployeeStore.FIELD_BIRTH_DAY, "19891123",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[0], store.search(EmployeeStore.FIELD_BIRTH_DAY, "19840308",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19940528",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"101", "102", "150", "190"}, store.search(EmployeeStore.FIELD_BIRTH_DAY,
                "19770530", OptionType.NO_OPTION , OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 certi modify 확인")
    void testModifyCerti() {
        verify(new String[]{"101", "110"}, store.modify(
                store.search(EmployeeStore.FIELD_CERTI, "EX", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_CERTI, "PRO")));
        verify(new String[0], store.search(EmployeeStore.FIELD_CERTI, "EX",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "101", "103", "110", "180", "200", "800"},
                store.search(EmployeeStore.FIELD_CERTI, "PRO", OptionType.NO_OPTION, OptionType.NO_OPTION));

        verify(new String[]{"103", "110", "150"}, store.modify(
                store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL3", OptionType.NO_OPTION, OptionType.NO_OPTION),
                Pair.create(EmployeeStore.FIELD_CERTI, "EX")));
        verify(new String[]{"102", "190"}, store.search(EmployeeStore.FIELD_CERTI, "ADV",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "101", "180", "200", "800"}, store.search(EmployeeStore.FIELD_CERTI, "PRO", OptionType.NO_OPTION,OptionType.NO_OPTION));
        verify(new String[]{"103", "110", "150"}, store.search(EmployeeStore.FIELD_CERTI, "EX",
                OptionType.NO_OPTION ,OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 없는 field modify 시 exception 확인")
    void testExceptionWhenNoField() {
        assertThrows(IllegalArgumentException.class,
                () -> store.modify(
                        store.search(EmployeeStore.FIELD_NAME, "DKD LEE", OptionType.NO_OPTION, OptionType.NO_OPTION),
                        Pair.create("field", "B")),
                "Invalid field: field");
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 searchAnd 확인")
    void testSearchAnd() {
        verify(new String[]{"102"}, store.searchAnd(
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL2"),
                Pair.create(EmployeeStore.FIELD_NAME, "IIIE PARK"),
                group)
        );
        verify(new String[]{"190"}, store.searchAnd(
                Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "19940528"),
                Pair.create(EmployeeStore.FIELD_CERTI, "ADV"),
                group)
        );
        verify(new String[0], store.searchAnd(
                Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-4485-1258"),
                Pair.create(EmployeeStore.FIELD_NAME, "DOFE LEE"),
                group)
        );
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 searchOr 확인")
    void testSearchOr() {
        verify(new String[]{"100", "110", "180"}, store.searchOr(
                Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "20000428"),
                Pair.create(EmployeeStore.FIELD_NAME, "DKD LEE"),
                group).stream().sorted(Comparator.comparing(Employee::getEmployeeNumber)).collect(Collectors.toList())
        );
        verify(new String[]{"101", "110", "190", "800"}, store.searchOr(
                Pair.create(EmployeeStore.FIELD_CERTI, "EX"),
                Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000"),
                group).stream().sorted(Comparator.comparing(Employee::getEmployeeNumber)).collect(Collectors.toList())
        );
        verify(new String[0], store.searchOr(
                Pair.create(EmployeeStore.FIELD_NAME, "FEFEF XXXXXXX"),
                Pair.create(EmployeeStore.FIELD_EMPLOYEE_NUMBER, "9023"),
                group)
        );
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 deleteAnd 확인")
    void testDeleteAnd() {
        verify(new String[]{"190", "800"}, store.delete(
                store.searchAnd(Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL2"),
                        Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000"),
                        group)
                )
        );
        assertEquals(8, store.count());

        verify(new String[]{"101"}, store.delete(
                store.searchAnd(Pair.create(EmployeeStore.FIELD_CERTI, "EX"),
                Pair.create(EmployeeStore.FIELD_NAME, "DOFE LEE"),
                group)));
        assertEquals(7, store.count());

        verify(new String[0], store.delete(
                store.searchAnd(
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL2"),
                Pair.create(EmployeeStore.FIELD_CERTI, "EX"),
                group)));
        assertEquals(7, store.count());
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 deleteOr 확인")
    void testDeleteOr() {
        verify(new String[]{"100", "101", "110", "190"}, store.delete(
                store.searchOr(Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "19940528"),
                Pair.create(EmployeeStore.FIELD_CERTI, "EX"),
                group).stream().sorted(Comparator.comparing(Employee::getEmployeeNumber)).collect(Collectors.toList())));
        assertEquals(6, store.count());

        verify(new String[]{"103", "150"}, store.delete(
                store.searchOr(Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL3"),
                Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-4535-4538"),
                group).stream().sorted(Comparator.comparing(Employee::getEmployeeNumber)).collect(Collectors.toList())));
        assertEquals(4, store.count());
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 modifyAnd 확인")
    void testModifyAnd() {
        verify(new String[]{"190"}, store.modify(
                store.searchAnd(Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-8787-0000"),
                Pair.create(EmployeeStore.FIELD_CERTI, "ADV"), group),
                Pair.create(EmployeeStore.FIELD_NAME, "LOW PARK")));
        verify(new String[0], store.search(EmployeeStore.FIELD_NAME,
                "LOW EFE", OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"190"}, store.search(EmployeeStore.FIELD_NAME,
                "LOW PARK", OptionType.NO_OPTION, OptionType.NO_OPTION));

        verify(new String[]{"100", "190"}, store.modify(
                store.searchAnd(Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "19940528"),
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL2"), group),
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL4")));
        verify(new String[]{"102", "180", "200", "800"}, store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL2",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"100", "101", "190"}, store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL4",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
    }

    @Test
    @DisplayName("EmployeeStoreImpl 내 modifyOr 확인")
    void testModifyOr() {
        verify(new String[]{"150", "200"}, store.modify(
                store.searchOr(Pair.create(EmployeeStore.FIELD_NAME, "WOWKF GWARK"),
                Pair.create(EmployeeStore.FIELD_PHONE_NUMBER, "010-7250-7824"), group),
                Pair.create(EmployeeStore.FIELD_BIRTH_DAY, "19881231")
                ).stream().sorted(Comparator.comparing(Employee::getEmployeeNumber)).collect(Collectors.toList()));
        verify(new String[0], store.search(EmployeeStore.FIELD_BIRTH_DAY, "19840308",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[0], store.search(EmployeeStore.FIELD_BIRTH_DAY, "19900101",
                OptionType.NO_OPTION, OptionType.NO_OPTION));
        verify(new String[]{"150", "200"}, store.search(EmployeeStore.FIELD_BIRTH_DAY, "19881231",
                OptionType.NO_OPTION, OptionType.NO_OPTION));

        verify(new String[]{"101", "103", "110", "150"}, store.modify(
                store.searchOr(Pair.create(EmployeeStore.FIELD_CERTI, "EX"),
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL3"), group),
                Pair.create(EmployeeStore.FIELD_CAREER_LEVEL, "CL2")
                ).stream().sorted(Comparator.comparing(Employee::getEmployeeNumber)).collect(Collectors.toList()));
        assertEquals(store.count(), store.search(EmployeeStore.FIELD_CAREER_LEVEL, "CL2",
                OptionType.NO_OPTION, OptionType.NO_OPTION).size());
    }
}
