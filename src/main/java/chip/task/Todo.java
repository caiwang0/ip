package chip.task;

/**
 * Represents a simple todo task without any time constraints.
 * Extends the base Task class to provide todo-specific formatting.
 */
public class Todo extends Task {

    /**
     * Constructs a new Todo task with the given description.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the file format string for this todo task.
     * Format: "T | status | description"
     *
     * @return the file format string representation
     */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    /**
     * Returns the display format string for this todo task.
     * Format: "[T][status] description"
     *
     * @return the display format string representation
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}