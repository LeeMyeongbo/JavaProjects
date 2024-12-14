package com.sec.bestreviewer;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Printer {

    private final String outputFilName;
    private final StringBuilder stdStringBuilder;

    public Printer(String outputFilename) {
        this(outputFilename, new StringBuilder());
    }

    public Printer(String outputFilename, StringBuilder stdStringBuilder) {
        this.outputFilName = outputFilename;
        this.stdStringBuilder = stdStringBuilder;
    }

    public void add(List<String> lines) {
        for (String line : lines) {
            stdStringBuilder.append(line).append("\n");
        }
    }

    public void printOutputFile() {
        try (OutputStream output = Files.newOutputStream(Paths.get(outputFilName))) {
            output.write(stdStringBuilder.toString().getBytes());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
