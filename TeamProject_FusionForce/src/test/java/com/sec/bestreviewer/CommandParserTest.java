package com.sec.bestreviewer;

import com.sec.bestreviewer.util.OptionParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    @Test
    @DisplayName("옵션이 없는 CMD를 잘파싱하는지 확인")
    void testParsingWithoutOptions() {
        String line = "ADD, , ,,08951033,QDJPTOJ KIM,CL3,010-3240-5443,19800308,PRO";

        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);


        assertEquals("ADD", tokens.getType());

        String[] options = {};
        assertEquals(3, tokens.getOptions().toArray().length);

        String[] params = {"08951033", "QDJPTOJ KIM", "CL3", "010-3240-5443", "19800308", "PRO"};
        assertArrayEquals(params, tokens.getParams().toArray());
    }

    @Test
    @DisplayName("옵션이 있는 CMD를 잘파싱하는지 확인")
    void testParsingWithOptions() {
        String line = "SCH,-p, , ,phoneNum,010-2742-2901";

        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);


        assertEquals("SCH", tokens.getType());

        String[] options = {"-p"};
        OptionParser optionParser = new OptionParser(tokens.getOptions());
        assertTrue(optionParser.isPrintOn());

        String[] params = {"phoneNum", "010-2742-2901"};
        assertArrayEquals(params, tokens.getParams().toArray());
    }

    @Test
    @DisplayName("-a 옵션이 있는 경우 파싱 확인")
    void testOptionParserWithAndCondition() {
        String line = "MOD,-p, , ,cl,CL2,-a,-m, ,birthday,01,name,YUJIN LEE";
        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);
        OptionParser parser = new OptionParser(tokens.getOptions());

        assertTrue(parser.isPrintOn());
        assertTrue(parser.isAndCondition());
        assertFalse(parser.isOrCondition());
    }

    @Test
    @DisplayName("-o 옵션이 있는 경우 파싱 확인")
    void testOptionParserWithOrCondition() {
        String line = "MOD, ,-d, ,birhday,06,-o, , ,certi,PRO,birthday,19901225";
        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);
        OptionParser parser = new OptionParser(tokens.getOptions());

        assertFalse(parser.isPrintOn());
        assertFalse(parser.isAndCondition());
        assertTrue(parser.isOrCondition());
    }

    @Test
    @DisplayName("-a, -o 옵션이 없는 경우 파싱 확인")
    void testOptionParserWithoutOptions() {
        String line = "MOD, ,-d, ,birhday,06,birthday,19901225";
        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);
        OptionParser parser = new OptionParser(tokens.getOptions());

        assertFalse(parser.isPrintOn());
        assertFalse(parser.isAndCondition());
        assertFalse(parser.isOrCondition());
    }

    @ParameterizedTest
    @MethodSource("getSymbolData")
    @DisplayName("부등호 옵션이 있는 경우 파싱 확인")
    void testSymbolParameter(String param) {
        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(param);
        boolean isSymbolContained = tokens.getOptions().contains("-ge") || tokens.getOptions().contains("-g")
                || tokens.getOptions().contains("-se") || tokens.getOptions().contains("-s");
        assertTrue(isSymbolContained);
    }

    static List<String> getSymbolData() {
        List<String> list = new ArrayList<>();
        list.add("SCH,-p,-f,-ge,name,YUJIN");
        list.add("SCH,-p, ,-s,cl,CL3");
        list.add("SCH,-p,-m,-se,phoneNum,0977");
        list.add("SCH,-p,-y,-ge,birthday,1990");
        list.add("SCH,-p, ,-ge,certi,PRO");
        return list;
    }

    @Test
    @DisplayName("Argument format 길이가 잘못된 경우 확인")
    void testParseInvalidFormat() {
        String line = "MOD";
        CommandParser commandParser = new CommandParser();
        assertThrows(IllegalArgumentException.class, () -> {
            commandParser.parse(line);
        });
    }
}
