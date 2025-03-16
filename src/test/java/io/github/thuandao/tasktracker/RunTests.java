package io.github.thuandao.tasktracker;

import io.github.thuandao.tasktracker.model.TaskTest;
import io.github.thuandao.tasktracker.service.TaskServiceTest;
import io.github.thuandao.tasktracker.util.JsonUtilTest;

/**
 * Simple test runner class to manually run all tests.
 * This can be used to verify the functionality of the application
 * without relying on the Maven test runner.
 */
public class RunTests {

    public static void main(String[] args) {
        System.out.println("Running Task Tracker tests...");

        // Run Task model tests
        System.out.println("\n=== Running Task model tests ===");
        TaskTest taskTest = new TaskTest();
        try {
            taskTest.testDefaultConstructor();
            taskTest.testParameterizedConstructor();
            taskTest.testSetDescription_UpdatesTimestamp();
            taskTest.testSetStatus_UpdatesTimestamp();
            taskTest.testToString();
            System.out.println("All Task model tests passed!");
        } catch (Exception e) {
            System.err.println("Task model tests failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Run JsonUtil tests
        System.out.println("\n=== Running JsonUtil tests ===");
        JsonUtilTest jsonUtilTest = new JsonUtilTest();
        try {
            jsonUtilTest.testToJson_SingleTask();
            jsonUtilTest.testToJson_TaskList();
            jsonUtilTest.testFromJson();
            jsonUtilTest.testFromJson_EmptyArray();
            jsonUtilTest.testFromJson_NullOrEmpty();
            jsonUtilTest.testJsonEscaping();
            System.out.println("All JsonUtil tests passed!");
        } catch (Exception e) {
            System.err.println("JsonUtil tests failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Run TaskService tests
        System.out.println("\n=== Running TaskService tests ===");
        TaskServiceTest taskServiceTest = new TaskServiceTest();
        try {
            taskServiceTest.setUp();
            taskServiceTest.testAddTask();
            taskServiceTest.testAddMultipleTasks();
            taskServiceTest.testUpdateTask();
            taskServiceTest.testUpdateNonExistentTask();
            taskServiceTest.testDeleteTask();
            taskServiceTest.testDeleteNonExistentTask();
            taskServiceTest.testUpdateTaskStatus();
            taskServiceTest.testListTasksWithStatusFilter();
            System.out.println("All TaskService tests passed!");
        } catch (Exception e) {
            System.err.println("TaskService tests failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Run TaskCliRunner tests
        System.out.println("\n=== Running TaskCliRunner tests ===");
        TaskCliRunnerTest taskCliRunnerTest = new TaskCliRunnerTest();
        try {
            taskCliRunnerTest.setUpStreams();
            taskCliRunnerTest.testAddCommand();
            taskCliRunnerTest.testUpdateCommand();
            taskCliRunnerTest.testDeleteCommand();
            taskCliRunnerTest.testMarkInProgressCommand();
            taskCliRunnerTest.testMarkDoneCommand();
            taskCliRunnerTest.testListCommand();
            taskCliRunnerTest.testListWithStatusFilter();
            taskCliRunnerTest.testNoTasksFound();
            taskCliRunnerTest.testInvalidCommand();
            taskCliRunnerTest.testMissingArguments();
            taskCliRunnerTest.testTaskNotFound();
            taskCliRunnerTest.testInvalidTaskId();
            taskCliRunnerTest.testFileIOError();
            taskCliRunnerTest.restoreStreams();
            System.out.println("All TaskCliRunner tests passed!");
        } catch (Exception e) {
            System.err.println("TaskCliRunner tests failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nAll tests completed!");
    }
}