package utils;


import com.github.cb2222124.rental.utils.CommandParser;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandParserTests {

    @Test
    public void empty() {
        String string = "";
        CommandParser parser = new CommandParser();
        assertEquals("", parser.getCommand(string));
    }

    @Test
    public void command() {
        String string = "login";
        CommandParser parser = new CommandParser();
        assertEquals("login", parser.getCommand(string));
    }

    @Test
    public void quotedCommand() {
        String string = "\"login\"";
        CommandParser parser = new CommandParser();
        assertEquals("login", parser.getCommand(string));
    }

    @Test
    public void contaminatedCommand() {
        String string = "\"login";
        CommandParser parser = new CommandParser();
        assertEquals("login", parser.getCommand(string));
    }

    @Test
    public void commandWithOtherTokens() {
        String string = "login one \"two and three\"";
        CommandParser parser = new CommandParser();
        assertEquals("login", parser.getCommand(string));
    }

    @Test
    public void commandWithArgumentsAndTokens() {
        String string = "login -one value \"two and three\"";
        CommandParser parser = new CommandParser();
        assertEquals("login", parser.getCommand(string));
    }

    @Test
    public void argument() {
        String string = "login -one";
        CommandParser parser = new CommandParser();
        HashMap<String, String> expected = new HashMap<>();
        expected.put("one", "");
        assertEquals(expected, parser.getArguments(string));
    }

    @Test
    public void argumentAndValue() {
        String string = "login -one value";
        CommandParser parser = new CommandParser();
        HashMap<String, String> expected = new HashMap<>();
        expected.put("one", "value");
        assertEquals(expected, parser.getArguments(string));
    }

    @Test
    public void argumentsAndValues() {
        String string = "login -one value -two \"second value\"";
        CommandParser parser = new CommandParser();
        HashMap<String, String> expected = new HashMap<>();
        expected.put("one", "value");
        expected.put("two", "second value");
        assertEquals(expected, parser.getArguments(string));
    }

    @Test
    public void argumentsAndValuesAndTokens() {
        String string = "login -one value -two \"second value\" random tokens -three value3";
        CommandParser parser = new CommandParser();
        HashMap<String, String> expected = new HashMap<>();
        expected.put("one", "value");
        expected.put("two", "second value");
        expected.put("three", "value3");
        assertEquals(expected, parser.getArguments(string));
    }
}
