package com.stockflow.utils;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtil {
    private CsvUtil() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean requiresQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");
        String escaped = value.replace("\"", "\"\"");
        return requiresQuotes ? "\"" + escaped + "\"" : escaped;
    }

    public static List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        values.add(current.toString());
        return values;
    }
}
