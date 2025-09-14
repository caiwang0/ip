package chip.command;

import java.util.ArrayList;

import chip.ChipException;
import chip.storage.Storage;
import chip.task.Deadline;
import chip.task.Event;
import chip.task.Task;
import chip.task.TaskList;
import chip.task.Todo;
import chip.ui.Ui;

/**
 * Handles parsing and execution of user commands.
 * Converts user input strings into appropriate actions on tasks, UI, and storage.
 */
public class Parser {
    
    // Constants for command parsing
    private static final String COMMAND_SEPARATOR = " ";
    private static final int COMMAND_PART_LIMIT = 2;
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";
    private static final String FILE_SEPARATOR = " \\| ";
    
    // Error messages
    private static final String ERROR_MARK_TASK = "Please specify which task to mark.";
    private static final String ERROR_UNMARK_TASK = "Please specify which task to unmark.";
    private static final String ERROR_DELETE_TASK = "Please specify which task to delete.";
    private static final String ERROR_TODO_EMPTY = "The description of a todo cannot be empty.";
    private static final String ERROR_DEADLINE_EMPTY = "The description of a deadline cannot be empty.";
    private static final String ERROR_DEADLINE_FORMAT = "Please specify the deadline time using /by.";
    private static final String ERROR_EVENT_EMPTY = "The description of an event cannot be empty.";
    private static final String ERROR_EVENT_FROM_FORMAT = "Please specify the event start time using /from.";
    private static final String ERROR_EVENT_TO_FORMAT = "Please specify the event end time using /to.";
    private static final String ERROR_FIND_KEYWORD = "Please specify a keyword to search for.";
    
    // Success messages
    private static final String MESSAGE_TASK_MARKED = "Nice! I've marked this task as done:";
    private static final String MESSAGE_TASK_UNMARKED = "OK, I've marked this task as not done yet:";
    private static final String MESSAGE_TASK_DELETED = "Noted. I've removed this task:";
    private static final String MESSAGE_TASK_ADDED = "Got it. I've added this task:";
    private static final String MESSAGE_TASK_COUNT = "Now you have %d tasks in the list.";
    private static final String MESSAGE_LIST_HEADER = "Here are the tasks in your list:";
    private static final String MESSAGE_FIND_HEADER = "Here are the matching tasks in your list:";
    private static final String MESSAGE_NO_MATCHES = "No matching tasks found.";

    /**
     * Parses a user command and executes the corresponding action.
     *
     * @param fullCommand the complete command string entered by the user
     * @param tasks the task list to operate on
     * @param ui the user interface for displaying messages
     * @param storage the storage component for saving tasks
     * @throws ChipException if the command is invalid or cannot be executed
     */
    public static void parse(String fullCommand, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        String[] parts = fullCommand.split(COMMAND_SEPARATOR, COMMAND_PART_LIMIT);
        Command action = Command.valueOf(parts[0].toUpperCase());

        switch (action) {
        case LIST:
            showTaskList(tasks, ui);
            break;
        case MARK:
            markTask(parts, tasks, ui, storage);
            break;
        case UNMARK:
            unmarkTask(parts, tasks, ui, storage);
            break;
        case DELETE:
            deleteTask(parts, tasks, ui, storage);
            break;
        case TODO:
            addTodo(parts, tasks, ui, storage);
            break;
        case DEADLINE:
            addDeadline(parts, tasks, ui, storage);
            break;
        case EVENT:
            addEvent(parts, tasks, ui, storage);
            break;
        case FIND:
            findTasks(parts, tasks, ui);
            break;
        }
    }

    /**
     * Displays all tasks in the task list with their index numbers.
     *
     * @param tasks the task list to display
     * @param ui the user interface for showing messages
     */
    private static void showTaskList(TaskList tasks, Ui ui) {
        ui.showMessage(MESSAGE_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    /**
     * Marks a specified task as completed.
     *
     * @param parts command parts where parts[1] should contain the task number
     * @param tasks the task list containing the task to mark
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving changes
     * @throws ChipException if task number is not specified or invalid
     */
    private static void markTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        validateCommandParts(parts, ERROR_MARK_TASK);
        int taskNumber = parseTaskNumber(parts[1]);
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        showTaskOperationResult(ui, MESSAGE_TASK_MARKED, task);
        storage.save(tasks.getTasks());
    }

    /**
     * Marks a specified task as not completed.
     *
     * @param parts command parts where parts[1] should contain the task number
     * @param tasks the task list containing the task to unmark
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving changes
     * @throws ChipException if task number is not specified or invalid
     */
    private static void unmarkTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        validateCommandParts(parts, ERROR_UNMARK_TASK);
        int taskNumber = parseTaskNumber(parts[1]);
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        showTaskOperationResult(ui, MESSAGE_TASK_UNMARKED, task);
        storage.save(tasks.getTasks());
    }

    /**
     * Deletes a specified task from the task list.
     *
     * @param parts command parts where parts[1] should contain the task number
     * @param tasks the task list to delete from
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving changes
     * @throws ChipException if task number is not specified or invalid
     */
    private static void deleteTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        validateCommandParts(parts, ERROR_DELETE_TASK);
        int taskNumber = parseTaskNumber(parts[1]);
        Task removedTask = tasks.deleteTask(taskNumber);
        showTaskOperationResult(ui, MESSAGE_TASK_DELETED, removedTask);
        ui.showMessage(String.format(MESSAGE_TASK_COUNT, tasks.size()));
        storage.save(tasks.getTasks());
    }

    /**
     * Adds a new todo task to the task list.
     *
     * @param parts command parts where parts[1] should contain the task description
     * @param tasks the task list to add to
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving changes
     * @throws ChipException if task description is empty
     */
    private static void addTodo(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        validateCommandParts(parts, ERROR_TODO_EMPTY);
        Task newTodo = new Todo(parts[1]);
        tasks.addTask(newTodo);
        showTaskAddedResult(ui, newTodo, tasks.size());
        storage.save(tasks.getTasks());
    }

    /**
     * Adds a new deadline task to the task list.
     *
     * @param parts command parts containing description and deadline in format "description /by deadline"
     * @param tasks the task list to add to
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving changes
     * @throws ChipException if description is empty or deadline format is invalid
     */
    private static void addDeadline(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("The description of a deadline cannot be empty.");
        }
        String[] deadlineParts = parts[1].split(" /by ");
        if (deadlineParts.length < 2) {
            throw new ChipException("Please specify the deadline time using /by.");
        }
        Task newDeadline = new Deadline(deadlineParts[0], deadlineParts[1]);
        tasks.addTask(newDeadline);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newDeadline);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    /**
     * Adds a new event task to the task list.
     *
     * @param parts command parts containing description and times in format "description /from start /to end"
     * @param tasks the task list to add to
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving changes
     * @throws ChipException if description is empty or time format is invalid
     */
    private static void addEvent(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        validateCommandParts(parts, ERROR_EVENT_EMPTY);
        String[] eventParts = parts[1].split(EVENT_FROM_SEPARATOR);
        if (eventParts.length < COMMAND_PART_LIMIT) {
            throw new ChipException(ERROR_EVENT_FROM_FORMAT);
        }
        String[] timeParts = eventParts[1].split(EVENT_TO_SEPARATOR);
        if (timeParts.length < COMMAND_PART_LIMIT) {
            throw new ChipException(ERROR_EVENT_TO_FORMAT);
        }
        Task newEvent = new Event(eventParts[0], timeParts[0], timeParts[1]);
        tasks.addTask(newEvent);
        showTaskAddedResult(ui, newEvent, tasks.size());
        storage.save(tasks.getTasks());
    }

    /**
     * Finds and displays tasks that contain the specified keyword.
     *
     * @param parts command parts where parts[1] should contain the search keyword
     * @param tasks the task list to search in
     * @param ui the user interface for showing messages
     * @throws ChipException if no keyword is provided
     */
    private static void findTasks(String[] parts, TaskList tasks, Ui ui) throws ChipException {
        validateCommandParts(parts, ERROR_FIND_KEYWORD);
        String keyword = parts[1];
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            ui.showMessage(MESSAGE_NO_MATCHES);
        } else {
            ui.showMessage(MESSAGE_FIND_HEADER);
            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }
    
    /**
     * Validates that command parts contain the required arguments.
     *
     * @param parts the command parts to validate
     * @param errorMessage the error message to throw if validation fails
     * @throws ChipException if validation fails
     */
    private static void validateCommandParts(String[] parts, String errorMessage) throws ChipException {
        if (parts.length < COMMAND_PART_LIMIT) {
            throw new ChipException(errorMessage);
        }
    }
    
    /**
     * Parses a task number from string input, converting from 1-based to 0-based indexing.
     *
     * @param taskNumberStr the task number as a string
     * @return the task number as a 0-based index
     * @throws ChipException if the task number is invalid
     */
    private static int parseTaskNumber(String taskNumberStr) throws ChipException {
        try {
            int taskNumber = Integer.parseInt(taskNumberStr) - 1;
            if (taskNumber < 0) {
                throw new ChipException("Task number must be positive.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new ChipException("Please provide a valid task number.");
        }
    }
    
    /**
     * Shows the result of a task operation with consistent formatting.
     *
     * @param ui the user interface for showing messages
     * @param message the message to display
     * @param task the task that was operated on
     */
    private static void showTaskOperationResult(Ui ui, String message, Task task) {
        ui.showMessage(message);
        ui.showMessage("   " + task);
    }
    
    /**
     * Shows the result of adding a task with consistent formatting.
     *
     * @param ui the user interface for showing messages
     * @param task the task that was added
     * @param taskCount the current number of tasks
     */
    private static void showTaskAddedResult(Ui ui, Task task, int taskCount) {
        ui.showMessage(MESSAGE_TASK_ADDED);
        ui.showMessage("   " + task);
        ui.showMessage(String.format(MESSAGE_TASK_COUNT, taskCount));
    }
}