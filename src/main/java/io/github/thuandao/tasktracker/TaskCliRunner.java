package io.github.thuandao.tasktracker;

import io.github.thuandao.tasktracker.model.Task;
import io.github.thuandao.tasktracker.model.TaskStatus;
import io.github.thuandao.tasktracker.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Command-line runner that handles the task tracker CLI commands.
 * Implements the CommandLineRunner interface to run when the application
 * starts.
 */
@Component
public class TaskCliRunner implements CommandLineRunner {

    /**
     * Service that handles all task operations.
     */
    private final TaskService taskService;

    /**
     * Constructor that injects the TaskService dependency.
     * 
     * @param taskService The service to use for task operations
     */
    @Autowired
    public TaskCliRunner(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Runs when the application starts, processes command-line arguments.
     * 
     * @param args Command-line arguments passed to the application
     */
    @Override
    public void run(String... args) {
        try {
            // If no arguments provided, show usage instructions
            if (args.length == 0) {
                printUsage();
                return;
            }

            // Parse the command (first argument)
            String command = args[0].toLowerCase();
            switch (command) {
                case "add":
                    // Check if the add command has the required description argument
                    if (args.length != 2) {
                        System.out.println("Error: 'add' command requires a description");
                        return;
                    }
                    handleAdd(args[1]);
                    break;

                case "update":
                    // Check if the update command has the required ID and description arguments
                    if (args.length != 3) {
                        System.out.println("Error: 'update' command requires an ID and description");
                        return;
                    }
                    handleUpdate(Long.parseLong(args[1]), args[2]);
                    break;

                case "delete":
                    // Check if the delete command has the required ID argument
                    if (args.length != 2) {
                        System.out.println("Error: 'delete' command requires an ID");
                        return;
                    }
                    handleDelete(Long.parseLong(args[1]));
                    break;

                case "mark-in-progress":
                    // Check if the mark-in-progress command has the required ID argument
                    if (args.length != 2) {
                        System.out.println("Error: 'mark-in-progress' command requires an ID");
                        return;
                    }
                    handleMarkStatus(Long.parseLong(args[1]), TaskStatus.IN_PROGRESS);
                    break;

                case "mark-done":
                    // Check if the mark-done command has the required ID argument
                    if (args.length != 2) {
                        System.out.println("Error: 'mark-done' command requires an ID");
                        return;
                    }
                    handleMarkStatus(Long.parseLong(args[1]), TaskStatus.DONE);
                    break;

                case "list":
                    // Check if the list command has at most one argument (optional status filter)
                    if (args.length > 2) {
                        System.out.println("Error: 'list' command takes at most one argument");
                        return;
                    }

                    // Parse the optional status filter
                    TaskStatus filterStatus = null;
                    if (args.length == 2) {
                        switch (args[1].toLowerCase()) {
                            case "todo":
                                filterStatus = TaskStatus.TODO;
                                break;
                            case "in-progress":
                                filterStatus = TaskStatus.IN_PROGRESS;
                                break;
                            case "done":
                                filterStatus = TaskStatus.DONE;
                                break;
                            default:
                                System.out
                                        .println("Error: Invalid status filter. Use 'todo', 'in-progress', or 'done'");
                                return;
                        }
                    }
                    handleList(filterStatus);
                    break;

                default:
                    // Unknown command, show usage instructions
                    System.out.println("Error: Unknown command '" + command + "'");
                    printUsage();
            }
        } catch (NumberFormatException e) {
            // Handle invalid task ID format (not a number)
            System.out.println("Error: Invalid task ID format");
        } catch (IllegalArgumentException e) {
            // Handle business logic errors (e.g., task not found)
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            // Handle file I/O errors
            System.out.println("Error: Failed to access tasks file - " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.out.println("Error: An unexpected error occurred - " + e.getMessage());
        }
    }

    /**
     * Handles the "add" command to create a new task.
     * 
     * @param description The description of the task to add
     * @throws IOException If there's an error accessing the tasks file
     */
    private void handleAdd(String description) throws IOException {
        Task task = taskService.addTask(description);
        System.out.println("Task added successfully (ID: " + task.getId() + ")");
    }

    /**
     * Handles the "update" command to update a task's description.
     * 
     * @param id          The ID of the task to update
     * @param description The new description for the task
     * @throws IOException If there's an error accessing the tasks file
     */
    private void handleUpdate(long id, String description) throws IOException {
        taskService.updateTask(id, description);
        System.out.println("Task " + id + " updated successfully");
    }

    /**
     * Handles the "delete" command to delete a task.
     * 
     * @param id The ID of the task to delete
     * @throws IOException If there's an error accessing the tasks file
     */
    private void handleDelete(long id) throws IOException {
        taskService.deleteTask(id);
        System.out.println("Task " + id + " deleted successfully");
    }

    /**
     * Handles the "mark-in-progress" and "mark-done" commands to update a task's
     * status.
     * 
     * @param id     The ID of the task to update
     * @param status The new status for the task
     * @throws IOException If there's an error accessing the tasks file
     */
    private void handleMarkStatus(long id, TaskStatus status) throws IOException {
        taskService.updateTaskStatus(id, status);
        System.out.println("Task " + id + " marked as " + status.toString().toLowerCase().replace('_', '-'));
    }

    /**
     * Handles the "list" command to display tasks, optionally filtered by status.
     * 
     * @param status The status to filter by, or null to list all tasks
     * @throws IOException If there's an error accessing the tasks file
     */
    private void handleList(TaskStatus status) throws IOException {
        List<Task> tasks = taskService.listTasks(status);

        // If no tasks found, display a message and return
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
            return;
        }

        // Display the tasks with appropriate header based on filter
        String statusFilter = status != null ? " (" + status.toString().toLowerCase().replace('_', '-') + ")" : "";
        System.out.println("Tasks" + statusFilter + ":");
        System.out.println("-".repeat(40));
        for (Task task : tasks) {
            System.out.println(task.toString());
            System.out.println("-".repeat(40));
        }
    }

    /**
     * Prints usage instructions for the application.
     */
    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  task-cli add \"<description>\"");
        System.out.println("  task-cli update <id> \"<description>\"");
        System.out.println("  task-cli delete <id>");
        System.out.println("  task-cli mark-in-progress <id>");
        System.out.println("  task-cli mark-done <id>");
        System.out.println("  task-cli list");
        System.out.println("  task-cli list done");
        System.out.println("  task-cli list todo");
        System.out.println("  task-cli list in-progress");
    }
}