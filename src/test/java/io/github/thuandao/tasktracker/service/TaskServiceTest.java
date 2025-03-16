package io.github.thuandao.tasktracker.service;

import io.github.thuandao.tasktracker.model.Task;
import io.github.thuandao.tasktracker.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TaskService class.
 * Uses a temporary directory for file operations to avoid affecting the real
 * file system.
 */
public class TaskServiceTest {

    @TempDir
    Path tempDir;

    private Path tasksFilePath;
    private TaskService taskService;

    /**
     * Set up the test environment before each test.
     * Creates a temporary tasks file and initializes the TaskService.
     */
    @BeforeEach
    public void setUp() throws IOException {
        tasksFilePath = tempDir.resolve("tasks.json");

        // Create a custom TaskService that uses our test file
        taskService = new TaskService() {
            @Override
            protected Path getFilePath() {
                return tasksFilePath;
            }
        };
    }

    @Test
    public void testAddTask() throws IOException {
        // When
        Task task = taskService.addTask("Test Task");

        // Then
        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());

        // Verify file was created and contains the task
        assertTrue(Files.exists(tasksFilePath));
        String fileContent = Files.readString(tasksFilePath);
        assertTrue(fileContent.contains("\"id\": 1"));
        assertTrue(fileContent.contains("\"description\": \"Test Task\""));
    }

    @Test
    public void testAddMultipleTasks() throws IOException {
        // When
        Task task1 = taskService.addTask("Task 1");
        Task task2 = taskService.addTask("Task 2");
        Task task3 = taskService.addTask("Task 3");

        // Then
        assertEquals(1L, task1.getId());
        assertEquals(2L, task2.getId());
        assertEquals(3L, task3.getId());

        // Verify all tasks are in the file
        List<Task> tasks = taskService.listTasks(null);
        assertEquals(3, tasks.size());
    }

    @Test
    public void testUpdateTask() throws IOException {
        // Given
        Task task = taskService.addTask("Original Description");

        // When
        Task updatedTask = taskService.updateTask(task.getId(), "Updated Description");

        // Then
        assertEquals("Updated Description", updatedTask.getDescription());

        // Verify the task was updated in the file
        List<Task> tasks = taskService.listTasks(null);
        assertEquals(1, tasks.size());
        assertEquals("Updated Description", tasks.get(0).getDescription());
    }

    @Test
    public void testUpdateNonExistentTask() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTask(999L, "This task doesn't exist");
        });
    }

    @Test
    public void testDeleteTask() throws IOException {
        // Given
        Task task = taskService.addTask("Task to delete");

        // When
        taskService.deleteTask(task.getId());

        // Then
        List<Task> tasks = taskService.listTasks(null);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testDeleteNonExistentTask() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.deleteTask(999L);
        });
    }

    @Test
    public void testUpdateTaskStatus() throws IOException {
        // Given
        Task task = taskService.addTask("Task");

        // When
        Task inProgressTask = taskService.updateTaskStatus(task.getId(), TaskStatus.IN_PROGRESS);

        // Then
        assertEquals(TaskStatus.IN_PROGRESS, inProgressTask.getStatus());

        // When
        Task doneTask = taskService.updateTaskStatus(task.getId(), TaskStatus.DONE);

        // Then
        assertEquals(TaskStatus.DONE, doneTask.getStatus());

        // Verify the task status was updated in the file
        List<Task> tasks = taskService.listTasks(null);
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.DONE, tasks.get(0).getStatus());
    }

    @Test
    public void testListTasksWithStatusFilter() throws IOException {
        // Given
        taskService.addTask("Todo Task");

        Task inProgressTask = taskService.addTask("In Progress Task");
        taskService.updateTaskStatus(inProgressTask.getId(), TaskStatus.IN_PROGRESS);

        Task doneTask = taskService.addTask("Done Task");
        taskService.updateTaskStatus(doneTask.getId(), TaskStatus.DONE);

        // When & Then
        List<Task> allTasks = taskService.listTasks(null);
        assertEquals(3, allTasks.size());

        List<Task> todoTasks = taskService.listTasks(TaskStatus.TODO);
        assertEquals(1, todoTasks.size());
        assertEquals("Todo Task", todoTasks.get(0).getDescription());

        List<Task> inProgressTasks = taskService.listTasks(TaskStatus.IN_PROGRESS);
        assertEquals(1, inProgressTasks.size());
        assertEquals("In Progress Task", inProgressTasks.get(0).getDescription());

        List<Task> doneTasks = taskService.listTasks(TaskStatus.DONE);
        assertEquals(1, doneTasks.size());
        assertEquals("Done Task", doneTasks.get(0).getDescription());
    }
}