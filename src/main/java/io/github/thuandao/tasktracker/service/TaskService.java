package io.github.thuandao.tasktracker.service;

import io.github.thuandao.tasktracker.model.Task;
import io.github.thuandao.tasktracker.model.TaskStatus;
import io.github.thuandao.tasktracker.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * Service class that handles all task-related business logic.
 * Responsible for CRUD operations on tasks and persisting them to a JSON file.
 */
@Service
public class TaskService {
    /**
     * The filename where tasks will be stored.
     * Located in the current directory as specified in the requirements.
     */
    private static final String TASKS_FILE = "tasks.json";

    /**
     * The path to the tasks file.
     */
    private final Path filePath;

    /**
     * Constructor that initializes the service with the default tasks file path.
     */
    public TaskService() {
        this.filePath = getFilePath();
    }

    /**
     * Gets the path to the tasks file.
     * This method can be overridden in tests to use a different file path.
     * 
     * @return The path to the tasks file
     */
    protected Path getFilePath() {
        return Paths.get(TASKS_FILE);
    }

    /**
     * Adds a new task with the given description.
     * Generates a unique ID for the task and saves it to the file.
     * 
     * @param description The description of the task to add
     * @return The newly created task
     * @throws IOException If there's an error reading from or writing to the file
     */
    public Task addTask(String description) throws IOException {
        // Load existing tasks
        List<Task> tasks = loadTasks();

        // Generate a new ID (max existing ID + 1, or 1 if no tasks exist)
        long nextId = tasks.stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0) + 1;

        // Create and add the new task
        Task newTask = new Task(nextId, description);
        tasks.add(newTask);

        // Save the updated task list
        saveTasks(tasks);
        return newTask;
    }

    /**
     * Updates an existing task's description.
     * 
     * @param id          The ID of the task to update
     * @param description The new description for the task
     * @return The updated task
     * @throws IOException              If there's an error reading from or writing
     *                                  to the file
     * @throws IllegalArgumentException If no task with the given ID exists
     */
    public Task updateTask(long id, String description) throws IOException {
        // Load existing tasks
        List<Task> tasks = loadTasks();

        // Find the task with the given ID
        Task task = findTaskById(tasks, id);
        if (task != null) {
            // Update the description and save
            task.setDescription(description);
            saveTasks(tasks);
            return task;
        }
        throw new IllegalArgumentException("Task not found with ID: " + id);
    }

    /**
     * Deletes a task with the given ID.
     * 
     * @param id The ID of the task to delete
     * @throws IOException              If there's an error reading from or writing
     *                                  to the file
     * @throws IllegalArgumentException If no task with the given ID exists
     */
    public void deleteTask(long id) throws IOException {
        // Load existing tasks
        List<Task> tasks = loadTasks();

        // Find the task with the given ID
        Task task = findTaskById(tasks, id);
        if (task != null) {
            // Remove the task and save
            tasks.remove(task);
            saveTasks(tasks);
        } else {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }
    }

    /**
     * Updates a task's status.
     * 
     * @param id     The ID of the task to update
     * @param status The new status for the task
     * @return The updated task
     * @throws IOException              If there's an error reading from or writing
     *                                  to the file
     * @throws IllegalArgumentException If no task with the given ID exists
     */
    public Task updateTaskStatus(long id, TaskStatus status) throws IOException {
        // Load existing tasks
        List<Task> tasks = loadTasks();

        // Find the task with the given ID
        Task task = findTaskById(tasks, id);
        if (task != null) {
            // Update the status and save
            task.setStatus(status);
            saveTasks(tasks);
            return task;
        }
        throw new IllegalArgumentException("Task not found with ID: " + id);
    }

    /**
     * Lists all tasks, optionally filtered by status.
     * 
     * @param status The status to filter by, or null to list all tasks
     * @return A list of tasks matching the filter
     * @throws IOException If there's an error reading from the file
     */
    public List<Task> listTasks(TaskStatus status) throws IOException {
        // Load all tasks
        List<Task> tasks = loadTasks();

        // If a status filter is provided, filter the tasks
        if (status != null) {
            return tasks.stream()
                    .filter(task -> task.getStatus() == status)
                    .collect(Collectors.toList());
        }
        // Otherwise return all tasks
        return tasks;
    }

    /**
     * Loads tasks from the JSON file.
     * If the file doesn't exist, returns an empty list.
     * 
     * @return A list of tasks loaded from the file
     * @throws IOException If there's an error reading from the file
     */
    private List<Task> loadTasks() throws IOException {
        // If the file doesn't exist, return an empty list
        if (!Files.exists(filePath)) {
            return List.of();
        }

        // Read the file content and parse it as JSON
        String json = Files.readString(filePath);
        return JsonUtil.fromJson(json);
    }

    /**
     * Saves tasks to the JSON file.
     * 
     * @param tasks The list of tasks to save
     * @throws IOException If there's an error writing to the file
     */
    private void saveTasks(List<Task> tasks) throws IOException {
        // Convert tasks to JSON and write to the file
        String json = JsonUtil.toJson(tasks);
        Files.writeString(filePath, json);
    }

    /**
     * Finds a task by its ID in a list of tasks.
     * 
     * @param tasks The list of tasks to search
     * @param id    The ID to search for
     * @return The task with the given ID, or null if not found
     */
    private Task findTaskById(List<Task> tasks, long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }
}