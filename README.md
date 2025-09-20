# Chip - Task Management Application

This is a Java-based task management application called **Chip**. It's a personal assistant that helps users manage their tasks with both command-line and graphical user interfaces.

## Features

- **Task Management**: Create, mark, delete, and organize tasks
- **Task Types**: Support for todos, deadlines, and events
- **Search**: Find tasks by keyword
- **Sorting**: Sort tasks alphabetically
- **Data Persistence**: Tasks are automatically saved to file
- **Dual Interface**: Both command-line and GUI support

## Quick Start

### Prerequisites
- JDK 17 or higher
- JavaFX 21.0.2

### Running the Application

1. **Command Line Interface**:
   ```bash
   java -jar chip.jar
   ```

2. **Graphical User Interface**:
   ```bash
   java -cp "chip.jar:javafx/lib/*" chip.Launcher
   ```

## Available Commands

| Command | Description | Example |
|---------|-------------|---------|
| `todo <description>` | Add a simple task | `todo read a book` |
| `deadline <description> /by <date>` | Add a task with deadline | `deadline submit report /by 2024-12-31 1800` |
| `event <description> /from <start> /to <end>` | Add an event | `event meeting /from 2024-12-25 1400 /to 2024-12-25 1600` |
| `list` | Show all tasks | `list` |
| `mark <number>` | Mark task as done | `mark 1` |
| `unmark <number>` | Mark task as not done | `unmark 1` |
| `delete <number>` | Remove a task | `delete 1` |
| `find <keyword>` | Search for tasks | `find meeting` |
| `sort` | Sort tasks alphabetically | `sort` |
| `help` | Show help information | `help` |
| `bye` | Exit the application | `bye` |

## Architecture

The application follows a layered architecture:

- **UI Layer** (`chip.ui`): Handles user interface operations
- **Command Layer** (`chip.command`): Parses and executes user commands  
- **Task Layer** (`chip.task`): Manages task data and operations
- **Storage Layer** (`chip.storage`): Handles data persistence
- **Main Application** (`chip`): Coordinates all components

## Project Structure

```
ip/
├── src/main/java/chip/          # Main source code
│   ├── command/                 # Command parsing and execution
│   ├── task/                    # Task management classes
│   ├── storage/                 # Data persistence
│   └── ui/                      # User interface
├── src/main/resources/          # GUI resources (FXML, images)
├── src/test/java/               # Test files
├── data/                        # Task data storage
└── README.md                    # This file
```

## Development

### Building the Project
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Creating Executable JAR
```bash
./gradlew shadowJar
```

## Data Format

Tasks are stored in a simple text format:
```
T | 0 | task description
D | 1 | deadline description | 2024-12-31 1800
E | 0 | event description | 2024-12-25 1400 | 2024-12-25 1600
```

Where:
- First field: Task type (T=Todo, D=Deadline, E=Event)
- Second field: Status (0=not done, 1=done)
- Remaining fields: Description and time information

## Contributing

This project was enhanced using AI tools as part of the A-AiAssisted increment. Contributions and improvements are welcome!

## License

This project is part of the CS2103 course and follows the Duke project template.