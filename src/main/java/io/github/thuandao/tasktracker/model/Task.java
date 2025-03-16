package io.github.thuandao.tasktracker.model;

import java.time.LocalDateTime;

/**
 * Entity class representing a task in the task tracking system.
 * Contains all the required properties for a task as specified in the
 * requirements.
 */
public class Task {
    /**
     * Unique identifier for the task.
     * Used to reference tasks for update, delete, and status change operations.
     */
    private Long id;

    /**
     * Description of the task provided by the user.
     * Contains the details of what needs to be done.
     */
    private String description;

    /**
     * Current status of the task.
     * Can be TODO, IN_PROGRESS, or DONE.
     */
    private TaskStatus status;

    /**
     * Timestamp when the task was created.
     * Set automatically when a new task is created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the task was last updated.
     * Updated automatically when task properties change.
     */
    private LocalDateTime updatedAt;

    /**
     * Default constructor that initializes timestamps and sets default status to
     * TODO.
     */
    public Task() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = TaskStatus.TODO;
    }

    /**
     * Constructor that creates a task with the specified ID and description.
     * 
     * @param id          The unique identifier for the task
     * @param description The description of the task
     */
    public Task(Long id, String description) {
        this(); // Call default constructor to initialize timestamps and status
        this.id = id;
        this.description = description;
    }

    /**
     * Gets the task's unique identifier.
     * 
     * @return The task ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the task's unique identifier.
     * 
     * @param id The ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the task's description.
     * 
     * @return The task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task's description and updates the updatedAt timestamp.
     * 
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now(); // Update timestamp when description changes
    }

    /**
     * Gets the task's current status.
     * 
     * @return The task status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the task's status and updates the updatedAt timestamp.
     * 
     * @param status The new status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now(); // Update timestamp when status changes
    }

    /**
     * Gets the timestamp when the task was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the timestamp when the task was last updated.
     * 
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Provides a string representation of the task for display in the console.
     * 
     * @return A formatted string with all task details
     */
    @Override
    public String toString() {
        return String.format("ID: %d%nDescription: %s%nStatus: %s%nCreated: %s%nUpdated: %s%n",
                id, description, status, createdAt, updatedAt);
    }
}