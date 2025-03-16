package io.github.thuandao.tasktracker;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite to run all tests for the Task Tracker application.
 */
@Suite
@SelectPackages({
        "io.github.thuandao.tasktracker.model",
        "io.github.thuandao.tasktracker.service",
        "io.github.thuandao.tasktracker.util",
        "io.github.thuandao.tasktracker"
})
public class TaskTrackerTestSuite {
    // This class serves as a test suite container
}