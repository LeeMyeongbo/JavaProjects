package com.sec.bestreviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {

    private final static int MIN_TOKENS_NUM = 5;
    private final static String SPACE = " ";
    private final static String EMPTY = "";

    public TokenGroup parse(String line) throws IllegalArgumentException {
        String[] r = line.split(",", -1);

        if (r.length < MIN_TOKENS_NUM) {
            throw new IllegalArgumentException("wrong command format");
        }

        String type = r[0];
        List<String> options = new ArrayList<>();
        List<String> params = new ArrayList<>();

        if (r.length > 8 && (r[6].equals("-a") || r[6].equals("-o"))) {
            options.addAll(Arrays.asList(Arrays.copyOfRange(r, 1, 4)));
            options.addAll(Arrays.asList(Arrays.copyOfRange(r, 6, 9)));
            params.addAll(Arrays.asList(Arrays.copyOfRange(r, 4, 6)));
            params.addAll(Arrays.asList(Arrays.copyOfRange(r, 9, r.length)));
        } else {
            options.addAll(Arrays.asList(Arrays.copyOfRange(r, 1, 4)));
            params.addAll(Arrays.asList(Arrays.copyOfRange(r, 4, r.length)));
        }

        return new TokenGroup(type, options, getValidList(params));
    }

    private List<String> getValidList(List<String> list) {
        List<String> validList = new ArrayList<>();

        list.forEach(i -> {
            if (!EMPTY.equals(i) && !SPACE.equals(i)) {
                validList.add(i);
            }
        });

        return validList;
    }
}
