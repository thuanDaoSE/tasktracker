package io.github.thuandao.tasktracker.util;

import io.github.thuandao.tasktracker.model.Task;
import io.github.thuandao.tasktracker.model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JsonUtil class.
 */
public class JsonUtilTest {

    @Test
    public void testToJson_SingleTask() {
        // Given
        Task task = new Task(1L, "Test Task");

        // When
        String json = JsonUtil.toJson(task);

        // Then
        assertTrue(json.contains("\"id\": 1"));
        assertTrue(json.contains("\"description\": \"Test Task\""));
        assertTrue(json.contains("\"status\": \"TODO\""));
        assertTrue(json.contains("\"createdAt\""));
        assertTrue(json.contains("\"updatedAt\""));
    }

    @Test
    public void testToJson_TaskList() {
        // Given
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1"));
        tasks.add(new Task(2L, "Task 2"));

        // When
        String json = JsonUtil.toJson(tasks);

        // Then
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
        assertTrue(json.contains("\"id\": 1"));
        assertTrue(json.contains("\"description\": \"Task 1\""));
        assertTrue(json.contains("\"id\": 2"));
        assertTrue(json.contains("\"description\": \"Task 2\""));
    }

    @Test
    public void testFromJson() {
        // Given
        String json = "[\n" +
                "  {\"id\": 1, \"description\": \"Task 1\", \"status\": \"TODO\", \"createdAt\": \"2023-01-01T12:00:00\", \"updatedAt\": \"2023-01-01T12:00:00\"},\n"
                +
                "  {\"id\": 2, \"description\": \"Task 2\", \"status\": \"IN_PROGRESS\", \"createdAt\": \"2023-01-02T12:00:00\", \"updatedAt\": \"2023-01-02T12:00:00\"}\n"
                +
                "]";

        // When
        List<Task> tasks = JsonUtil.fromJson(json);

        // Then
        assertEquals(2, tasks.size());

        Task task1 = tasks.get(0);
        assertEquals(1L, task1.getId());
        assertEquals("Task 1", task1.getDescription());
        assertEquals(TaskStatus.TODO, task1.getStatus());

        Task task2 = tasks.get(1);
        assertEquals(2L, task2.getId());
        assertEquals("Task 2", task2.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task2.getStatus());
    }

    @Test
    public void testFromJson_EmptyArray() {
        // Given
        String json = "[]";

        // When
        List<Task> tasks = JsonUtil.fromJson(json);

        // Then
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testFromJson_NullOrEmpty() {
        // Given
        String nullJson = null;
        String emptyJson = "";
        String whitespaceJson = "   ";

        // When & Then
        assertTrue(JsonUtil.fromJson(nullJson).isEmpty());
        assertTrue(JsonUtil.fromJson(emptyJson).isEmpty());
        assertTrue(JsonUtil.fromJson(whitespaceJson).isEmpty());
    }

    @Test
    public void testJsonEscaping() {
        // Given
        Task task = new Task(1L, "Task with \"quotes\" and \nnewlines");

        // When
        String json = JsonUtil.toJson(task);
        List<Task> tasks = JsonUtil.fromJson("[" + json + "]");

        // Then
        assertEquals(1, tasks.size());
        assertEquals("Task with \"quotes\" and \nnewlines", tasks.get(0).getDescription());
    }
}