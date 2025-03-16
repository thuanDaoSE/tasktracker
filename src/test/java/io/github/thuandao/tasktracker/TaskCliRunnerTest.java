package io.github.thuandao.tasktracker;

import io.github.thuandao.tasktracker.model.Task;
import io.github.thuandao.tasktracker.model.TaskStatus;
import io.github.thuandao.tasktracker.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the TaskCliRunner class.
 * Uses Mockito to mock the TaskService to avoid file system operations.
 */
@ExtendWith(MockitoExtension.class)
public class TaskCliRunnerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskCliRunner taskCliRunner;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testAddCommand() throws IOException {
        // Given
        Task newTask = new Task(1L, "Test Task");
        when(taskService.addTask("Test Task")).thenReturn(newTask);

        // When
        taskCliRunner.run("add", "Test Task");

        // Then
        verify(taskService).addTask("Test Task");
        assertTrue(outContent.toString().contains("Task added successfully (ID: 1)"));
    }

    @Test
    public void testUpdateCommand() throws IOException {
        // Given
        Task updatedTask = new Task(1L, "Updated Task");
        when(taskService.updateTask(1L, "Updated Task")).thenReturn(updatedTask);

        // When
        taskCliRunner.run("update", "1", "Updated Task");

        // Then
        verify(taskService).updateTask(1L, "Updated Task");
        assertTrue(outContent.toString().contains("Task 1 updated successfully"));
    }

    @Test
    public void testDeleteCommand() throws IOException {
        // When
        taskCliRunner.run("delete", "1");

        // Then
        verify(taskService).deleteTask(1L);
        assertTrue(outContent.toString().contains("Task 1 deleted successfully"));
    }

    @Test
    public void testMarkInProgressCommand() throws IOException {
        // Given
        Task task = new Task(1L, "Test Task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS)).thenReturn(task);

        // When
        taskCliRunner.run("mark-in-progress", "1");

        // Then
        verify(taskService).updateTaskStatus(1L, TaskStatus.IN_PROGRESS);
        assertTrue(outContent.toString().contains("Task 1 marked as in-progress"));
    }

    @Test
    public void testMarkDoneCommand() throws IOException {
        // Given
        Task task = new Task(1L, "Test Task");
        task.setStatus(TaskStatus.DONE);
        when(taskService.updateTaskStatus(1L, TaskStatus.DONE)).thenReturn(task);

        // When
        taskCliRunner.run("mark-done", "1");

        // Then
        verify(taskService).updateTaskStatus(1L, TaskStatus.DONE);
        assertTrue(outContent.toString().contains("Task 1 marked as done"));
    }

    @Test
    public void testListCommand() throws IOException {
        // Given
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1"));
        tasks.add(new Task(2L, "Task 2"));
        when(taskService.listTasks(null)).thenReturn(tasks);

        // When
        taskCliRunner.run("list");

        // Then
        verify(taskService).listTasks(null);
        String output = outContent.toString();
        assertTrue(output.contains("Tasks:"));
        assertTrue(output.contains("Task 1"));
        assertTrue(output.contains("Task 2"));
    }

    @Test
    public void testListWithStatusFilter() throws IOException {
        // Given
        List<Task> tasks = new ArrayList<>();
        Task task = new Task(1L, "Done Task");
        task.setStatus(TaskStatus.DONE);
        tasks.add(task);
        when(taskService.listTasks(TaskStatus.DONE)).thenReturn(tasks);

        // When
        taskCliRunner.run("list", "done");

        // Then
        verify(taskService).listTasks(TaskStatus.DONE);
        String output = outContent.toString();
        assertTrue(output.contains("Tasks (done):"));
        assertTrue(output.contains("Done Task"));
    }

    @Test
    public void testNoTasksFound() throws IOException {
        // Given
        when(taskService.listTasks(any())).thenReturn(new ArrayList<>());

        // When
        taskCliRunner.run("list");

        // Then
        assertTrue(outContent.toString().contains("No tasks found"));
    }

    @Test
    public void testInvalidCommand() {
        // When
        taskCliRunner.run("invalid");

        // Then
        assertTrue(outContent.toString().contains("Error: Unknown command 'invalid'"));
        assertTrue(outContent.toString().contains("Usage:"));
    }

    @Test
    public void testMissingArguments() {
        // When
        taskCliRunner.run("add");

        // Then
        assertTrue(outContent.toString().contains("Error: 'add' command requires a description"));
    }

    @Test
    public void testTaskNotFound() throws IOException {
        // Given
        doThrow(new IllegalArgumentException("Task not found with ID: 999"))
                .when(taskService).updateTask(eq(999L), any());

        // When
        taskCliRunner.run("update", "999", "This task doesn't exist");

        // Then
        assertTrue(outContent.toString().contains("Error: Task not found with ID: 999"));
    }

    @Test
    public void testInvalidTaskId() {
        // When
        taskCliRunner.run("update", "not-a-number", "Invalid ID");

        // Then
        assertTrue(outContent.toString().contains("Error: Invalid task ID format"));
    }

    @Test
    public void testFileIOError() throws IOException {
        // Given
        doThrow(new IOException("File access error"))
                .when(taskService).addTask(any());

        // When
        taskCliRunner.run("add", "Test Task");

        // Then
        assertTrue(outContent.toString().contains("Error: Failed to access tasks file"));
    }
}