package io.github.thuandao.tasktracker.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Unit tests for the Task model class.
 */
public class TaskTest {

    @Test
    public void testDefaultConstructor() {
        // When
        Task task = new Task();

        // Then
        assertNull(task.getId());
        assertNull(task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNotNull(task.getCreatedAt());
        assertEquals(task.getCreatedAt(), task.getUpdatedAt());
    }

    @Test
    public void testParameterizedConstructor() {
        // Given
        Long id = 1L;
        String description = "Test task";

        // When
        Task task = new Task(id, description);

        // Then
        assertEquals(id, task.getId());
        assertEquals(description, task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNotNull(task.getCreatedAt());
        assertEquals(task.getCreatedAt(), task.getUpdatedAt());
    }

    @Test
    public void testSetDescription_UpdatesTimestamp() throws InterruptedException {
        // Given
        Task task = new Task(1L, "Original description");
        LocalDateTime originalTimestamp = task.getUpdatedAt();

        // Ensure some time passes for timestamp to be different
        Thread.sleep(10);

        // When
        task.setDescription("Updated description");

        // Then
        assertEquals("Updated description", task.getDescription());
        assertNotEquals(originalTimestamp, task.getUpdatedAt());
    }

    @Test
    public void testSetStatus_UpdatesTimestamp() throws InterruptedException {
        // Given
        Task task = new Task(1L, "Test task");
        LocalDateTime originalTimestamp = task.getUpdatedAt();

        // Ensure some time passes for timestamp to be different
        Thread.sleep(10);

        // When
        task.setStatus(TaskStatus.IN_PROGRESS);

        // Then
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertNotEquals(originalTimestamp, task.getUpdatedAt());
    }

    @Test
    public void testToString() {
        // Given
        Task task = new Task(1L, "Test task");
        task.setStatus(TaskStatus.IN_PROGRESS);

        // When
        String result = task.toString();

        // Then
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("description=Test task"));
        assertTrue(result.contains("status=IN_PROGRESS"));
        assertTrue(result.contains("createdAt="));
        assertTrue(result.contains("updatedAt="));
    }
}