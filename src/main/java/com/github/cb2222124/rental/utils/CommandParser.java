package com.github.cb2222124.rental.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class designed to parse user input at the console into application command arguments.
 *
 * @author Callan.
 */
public class CommandParser {

    /**
     * Retrieves the first token found in a given string.
     *
     * @param string The string to be evaluated.
     * @return The first token.
     */
    public String getCommand(String string) {
        List<String> tokens = getTokens(string);
        if (tokens.size() == 0) return "";
        return tokens.get(0);
    }

    /**
     * Takes in a string and evaluates every token excluding the first for potential command arguments.
     * Duplicate arguments are overridden by the latter occurrence.
     * Example: "vehicle -year 1994 -type toyota supra" would produce {year=1994, type=toyota supra}.
     *
     * @param string The string to be evaluated.
     * @return A hashmap of arguments. See above example.
     */
    public HashMap<String, String> getArguments(String string) {
        List<String> tokens = getTokens(string);
        HashMap<String, String> arguments = new HashMap<>();
        boolean valueNext = false;
        for (int i = 1; i < tokens.size(); i++) {
            if (tokens.get(i).startsWith("-")) {
                arguments.put(tokens.get(i).substring(1), "");
                valueNext = true;
            } else if (valueNext) {
                arguments.put(tokens.get(i - 1), tokens.get(i));
                valueNext = false;
            }
        }
        return arguments;

    }

    /**
     * Splits a given string on spaces, excluding characters within quotes.
     *
     * @param string The string to be evaluated.
     * @return A list of tokenised strings.
     */
    private List<String> getTokens(String string) {
        List<String> list = new ArrayList<>();
        //Regular expression referenced: https://stackoverflow.com/a/7804472/16950454.
        Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(string);
        while (matcher.find()) {
            list.add(matcher.group(1).replace("\"", "").trim());
        }
        return list;
    }
}
