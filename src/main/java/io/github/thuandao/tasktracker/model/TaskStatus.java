package io.github.thuandao.tasktracker.model;

/**
 * Enum representing the possible states of a task in the task tracking system.
 * Provides a type-safe way to represent and validate task statuses.
 */
public enum TaskStatus {
    /**
     * Represents a task that has been created but not yet started.
     * This is the default status for new tasks.
     */
    TODO,

    /**
     * Represents a task that is currently being worked on.
     * Tasks move to this status when the user marks them as "in progress".
     */
    IN_PROGRESS,

    /**
     * Represents a task that has been completed.
     * Tasks move to this status when the user marks them as "done".
     */
    DONE
}