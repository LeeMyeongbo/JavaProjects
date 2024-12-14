package com.sec.bestreviewer.performance;

import com.sec.bestreviewer.EmployeeManagement;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.DecimalFormat;

public class EmployeeManagementPerformanceTest {

    @Test
    public void deletePerformanceTest() {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/performance/delete_performance_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/performance/delete_performance_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        long startTime = System.nanoTime();
        employeeManagement.run(args);
        long endTime = System.nanoTime();

        double durationSec = (double)(endTime - startTime) / 1000000000.0; // 나노초를 초로 변환
        DecimalFormat df = new DecimalFormat("#.###"); // 소수점 세 자리까지 출력
        System.out.println("Execution time: " + df.format(durationSec) + "s");

        Approvals.verify(outputFile);

    }

    @Test
    public void searchPerformanceTest() {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/performance/search_performance_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/performance/search_performance_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        long startTime = System.nanoTime();
        employeeManagement.run(args);
        long endTime = System.nanoTime();

        double durationSec = (double)(endTime - startTime) / 1000000000.0; // 나노초를 초로 변환
        DecimalFormat df = new DecimalFormat("#.###"); // 소수점 세 자리까지 출력
        System.out.println("Execution time: " + df.format(durationSec) + "s");

        Approvals.verify(outputFile);
    }

    @Test
    public void searchThirdOptionPerformanceTest() {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/performance/search_third_option_performance_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/performance/search_third_option_performance_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        long startTime = System.nanoTime();
        employeeManagement.run(args);
        long endTime = System.nanoTime();

        double durationSec = (double)(endTime - startTime) / 1000000000.0; // 나노초를 초로 변환
        DecimalFormat df = new DecimalFormat("#.###"); // 소수점 세 자리까지 출력
        System.out.println("Execution time: " + df.format(durationSec) + "s");

        Approvals.verify(outputFile);
    }

    @Test
    public void modifyPerformanceTest() {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/performance/modify_performance_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/performance/modify_performance_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        long startTime = System.nanoTime();
        employeeManagement.run(args);
        long endTime = System.nanoTime();

        double durationSec = (double)(endTime - startTime) / 1000000000.0; // 나노초를 초로 변환
        DecimalFormat df = new DecimalFormat("#.###"); // 소수점 세 자리까지 출력
        System.out.println("Execution time: " + df.format(durationSec) + "s");


        Approvals.verify(outputFile);
    }
}
