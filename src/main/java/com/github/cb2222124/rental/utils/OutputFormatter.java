package com.github.cb2222124.rental.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for printing information to the system output stream.
 *
 * @author Callan
 */
public class OutputFormatter {

    /**
     * Outputs specified columns of a result set in tabular form with provided column labels.
     *
     * @param result  The result set.
     * @param columns The names of the columns to fetch from the result set.
     * @param labels  The labels to be given to each column when outputting.
     * @throws SQLException Callers are responsible for handling database access issues.
     */
    public void printResultSet(ResultSet result, String[] columns, String[] labels) throws SQLException {
        List<List<String>> resultTable = new ArrayList<>();
        resultTable.add(Arrays.asList(labels));
        while (result.next()) {
            List<String> row = new ArrayList<>();
            for (String column : columns) {
                row.add(result.getString(column));
            }
            resultTable.add(row);
        }
        printTable(resultTable);
    }

    /**
     * Generic function to output a list of lists, or 'rows', in tabular format.
     *
     * @param table The list of lists to be outputted.
     * @param <T>   Generic 'cell' value.
     */
    public <T> void printTable(List<List<T>> table) {
        //First pass to establish column widths width.
        HashMap<Integer, Integer> columnWidths = new HashMap<>();
        for (List<T> row : table) {
            for (int i = 0; i < row.size(); i++) {
                int stringLength = row.get(i).toString().length();
                if (columnWidths.get(i) == null || stringLength > columnWidths.get(i))
                    columnWidths.put(i, stringLength);
            }
        }
        //Second pass to print values
        for (List<T> row : table) {
            for (int i = 0; i < row.size(); i++) {
                System.out.format("%-" + (columnWidths.get(i) + 4) + "s", row.get(i).toString());
            }
            System.out.println();
        }
    }
}
