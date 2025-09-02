package chip.command;

/**
 * Enumeration of all available commands in the Chip application.
 * Each enum value represents a specific action that users can perform.
 */
public enum Command {
    /** Add a simple todo task */
    TODO,
    /** Add a task with a deadline */
    DEADLINE,
    /** Add a task that occurs during a specific time period */
    EVENT,
    /** Display all tasks in the list */
    LIST,
    /** Mark a task as completed */
    MARK,
    /** Mark a task as not completed */
    UNMARK,
    /** Remove a task from the list */
    DELETE,
    /** Find tasks containing a keyword */
    FIND,
    /** Exit the application */
    BYE
}