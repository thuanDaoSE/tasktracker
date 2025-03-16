package io.github.thuandao.tasktracker.util;

import io.github.thuandao.tasktracker.model.Task;
import io.github.thuandao.tasktracker.model.TaskStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for JSON serialization and deserialization.
 * Implements custom JSON handling without using external libraries.
 * Provides methods to convert Task objects to JSON strings and vice versa.
 */
public class JsonUtil {
    /**
     * Formatter for converting LocalDateTime objects to ISO format strings.
     * Used for consistent date/time formatting in the JSON.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts a list of Task objects to a JSON array string.
     * 
     * @param tasks The list of tasks to convert to JSON
     * @return A formatted JSON array string representing the tasks
     */
    public static String toJson(List<Task> tasks) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            json.append("  ").append(toJson(tasks.get(i)));
            if (i < tasks.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Converts a single Task object to a JSON object string.
     * 
     * @param task The task to convert to JSON
     * @return A JSON object string representing the task
     */
    public static String toJson(Task task) {
        return String.format(
                "{\"id\": %d, \"description\": \"%s\", \"status\": \"%s\", \"createdAt\": \"%s\", \"updatedAt\": \"%s\"}",
                task.getId(),
                escapeJson(task.getDescription()), // Escape special characters in description
                task.getStatus(),
                task.getCreatedAt().format(formatter), // Format dates as ISO strings
                task.getUpdatedAt().format(formatter));
    }

    /**
     * Parses a JSON string into a list of Task objects.
     * Uses regex patterns to extract task data from the JSON.
     * 
     * @param json The JSON string to parse
     * @return A list of Task objects parsed from the JSON
     */
    public static List<Task> fromJson(String json) {
        List<Task> tasks = new ArrayList<>();

        // Handle empty or null JSON
        if (json == null || json.trim().isEmpty() || json.trim().equals("[]")) {
            return tasks;
        }

        // Use regex to find all JSON objects in the string
        Pattern taskPattern = Pattern.compile("\\{[^}]+\\}");
        Matcher matcher = taskPattern.matcher(json);

        // Parse each JSON object into a Task
        while (matcher.find()) {
            String taskJson = matcher.group();
            tasks.add(parseTask(taskJson));
        }

        return tasks;
    }

    /**
     * Parses a single JSON object string into a Task object.
     * Uses regex patterns to extract each field from the JSON.
     * 
     * @param json The JSON object string to parse
     * @return A Task object with properties set from the JSON
     */
    private static Task parseTask(String json) {
        Task task = new Task();

        // Parse ID using regex
        Pattern idPattern = Pattern.compile("\"id\":\\s*(\\d+)");
        Matcher idMatcher = idPattern.matcher(json);
        if (idMatcher.find()) {
            task.setId(Long.parseLong(idMatcher.group(1)));
        }

        // Parse description using regex
        Pattern descPattern = Pattern.compile("\"description\":\\s*\"([^\"]*)\"");
        Matcher descMatcher = descPattern.matcher(json);
        if (descMatcher.find()) {
            task.setDescription(unescapeJson(descMatcher.group(1)));
        }

        // Parse status using regex
        Pattern statusPattern = Pattern.compile("\"status\":\\s*\"([^\"]*)\"");
        Matcher statusMatcher = statusPattern.matcher(json);
        if (statusMatcher.find()) {
            task.setStatus(TaskStatus.valueOf(statusMatcher.group(1)));
        }

        // Parse dates using regex
        Pattern datePattern = Pattern.compile("\"(createdAt|updatedAt)\":\\s*\"([^\"]*)\"");
        Matcher dateMatcher = datePattern.matcher(json);
        while (dateMatcher.find()) {
            LocalDateTime dateTime = LocalDateTime.parse(dateMatcher.group(2), formatter);
            // If we found createdAt, recreate the task to set both createdAt and proper
            // defaults
            if (dateMatcher.group(1).equals("createdAt")) {
                task = new Task(task.getId(), task.getDescription());
            }
        }

        return task;
    }

    /**
     * Escapes special characters in a string for JSON compatibility.
     * Handles quotes, newlines, carriage returns, and tabs.
     * 
     * @param text The text to escape
     * @return The escaped text
     */
    private static String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\"", "\\\"") // Escape double quotes
                .replace("\n", "\\n") // Escape newlines
                .replace("\r", "\\r") // Escape carriage returns
                .replace("\t", "\\t"); // Escape tabs
    }

    /**
     * Unescapes special characters in a JSON string.
     * Reverses the escaping done by escapeJson.
     * 
     * @param text The JSON text to unescape
     * @return The unescaped text
     */
    private static String unescapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\\"", "\"") // Unescape double quotes
                .replace("\\n", "\n") // Unescape newlines
                .replace("\\r", "\r") // Unescape carriage returns
                .replace("\\t", "\t"); // Unescape tabs
    }
}