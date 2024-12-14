package com.sec.bestreviewer;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandReaderTest {

    @Test
    void testReadWhenFileExist() {
        String inputFileName = "command_reader_test_input.txt";
        String inputFilePath = "./src/test/java/com/sec/bestreviewer/" + inputFileName;
        CommandReader reader = new CommandReader(inputFilePath);

        try (BufferedReader output = new BufferedReader(
                new FileReader("./src/test/java/com/sec/bestreviewer/command_reader_test_output.txt"))) {
            List<String> lines = reader.readFile();
            for (String line : lines)
                assertEquals(line, output.readLine());
            assertNull(output.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}