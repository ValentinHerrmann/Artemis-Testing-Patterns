package de.tum.cit.aet.levenshtein;

import java.util.LinkedList;
import java.util.List;

public class Logging {
    private static List<String> logs = new LinkedList<>();

    private static void write(String message) {
        logs.add(message);
    }
    public static List<String> getLogs() {
        return logs;
    }

    public static void log(String message) {
        String formattedMessage = String.format("ℹ️ %s", message);
        write(formattedMessage);
    }

    public static void logWarning(String message) {
        String formattedMessage = String.format("⚠️ %s", message);
        write(formattedMessage);
    }

    public static void logError(String message) {
        String formattedMessage = String.format("❌ %s", message);
        write(formattedMessage);
    }
}
