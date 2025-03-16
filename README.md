# Task Tracker CLI

A command-line task management application built with Spring Boot that stores tasks in a JSON file.

Project based on: [roadmap.sh/projects/task-tracker](https://roadmap.sh/projects/task-tracker)

## Features

- Add, update, and delete tasks
- Mark tasks as in-progress or done
- List all tasks
- Filter tasks by status (todo, in-progress, done)
- Persistent storage using JSON file
- Comprehensive error handling

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Building the Application

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Running the Application

After building, you can run the application using:

```bash
java -jar target/tasktracker-0.0.1-SNAPSHOT.jar <command> [arguments]
```

For convenience, you can create a shell script or batch file named `task-cli` that runs the JAR file:

### For Linux/macOS (task-cli):

```bash
#!/bin/bash
java -jar /path/to/tasktracker-0.0.1-SNAPSHOT.jar "$@"
```

Make it executable:

```bash
chmod +x task-cli
```

### For Windows (task-cli.bat):

```batch
@echo off
java -jar C:\path\to\tasktracker-0.0.1-SNAPSHOT.jar %*
```

## Usage

### Adding a new task

```bash
task-cli add "Buy groceries"
```

### Updating a task

```bash
task-cli update 1 "Buy groceries and cook dinner"
```

### Deleting a task

```bash
task-cli delete 1
```

### Marking task status

```bash
task-cli mark-in-progress 1
task-cli mark-done 1
```

### Listing tasks

```bash
# List all tasks
task-cli list

# List tasks by status
task-cli list done
task-cli list todo
task-cli list in-progress
```

## Data Storage

Tasks are stored in a `tasks.json` file in the current directory. The file is created automatically when you add your first task.

Each task has the following properties:

- id: Unique identifier
- description: Task description
- status: Current status (todo, in-progress, done)
- createdAt: Creation timestamp
- updatedAt: Last update timestamp

## Error Handling

The application handles various error cases:

- Invalid commands or arguments
- Non-existent task IDs
- File system errors
- JSON parsing errors

## Implementation Details

The application follows a layered architecture:

1. **Model Layer**: Contains the Task entity and TaskStatus enum
2. **Service Layer**: Handles business logic and file operations
3. **CLI Layer**: Processes command-line arguments and displays results

The JSON file is managed using custom serialization/deserialization without external libraries, as per the requirements.

## Testing

The application includes comprehensive unit tests for all components:

- **TaskTest**: Tests for the Task model class
- **JsonUtilTest**: Tests for the JSON serialization/deserialization utility
- **TaskServiceTest**: Tests for the business logic service
- **TaskCliRunnerTest**: Tests for the command-line interface

To run the tests, use:

```bash
mvn test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
