package com.sec.bestreviewer.store;

import com.sec.bestreviewer.util.OptionType;

import java.util.Objects;

public class Employee {

    private final String employeeNumber;
    private String name;
    private String careerLevel;
    private String phoneNumber;
    private String birthday;
    private String certi;

    public Employee(String employeeNumber, String name, String careerLevel, String phoneNumber,
            String birthday, String certi) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.careerLevel = careerLevel;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.certi = certi;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getName() {
        return name;
    }

    public String getName(String secondOption) {
        String[] nameArray = name.split(" ");

        return switch (secondOption) {
            case OptionType.FIRST_NAME -> nameArray[0];
            case OptionType.LAST_NAME -> nameArray[1];
            default -> name;
        };
    }

    public String getCareerLevel() {
        return careerLevel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumber(String secondOption) {
        String[] phoneNumberArray = phoneNumber.split("-");

        return switch (secondOption) {
            case OptionType.MIDDLE_NUMBER -> phoneNumberArray[1];
            case OptionType.LAST_NUMBER -> phoneNumberArray[2];
            default -> phoneNumber;
        };
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthday(String secondOption) {
        return switch (secondOption) {
            case OptionType.YEAR -> birthday.substring(0, 4);
            case OptionType.MONTH -> birthday.substring(4, 6);
            case OptionType.DAY -> birthday.substring(6, 8);
            default -> birthday;
        };
    }

    public String getCerti() {
        return certi;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCareerLevel(String careerLevel) {
        this.careerLevel = careerLevel;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCerti(String certi) {
        this.certi = certi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Employee employee = (Employee) o;
        return Objects.equals(employeeNumber, employee.employeeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber);
    }
}
