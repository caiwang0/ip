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
 * Converts user input strings into appropriate actions on tasks, UI and storage.
 */
public class Parser {
    
    // Constants for command parsing
    private static final String COMMAND_SEPARATOR = " ";
    private static final int COMMAND_PART_LIMIT = 2;
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";
    
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
    private static final String ERROR_INVALID_TASK_NUMBER = "Please provide a valid task number.";
    private static final String ERROR_NEGATIVE_TASK_NUMBER = "Task number must be positive.";
    
    // Success messages
    private static final String MESSAGE_TASK_MARKED = "Nice! I've marked this task as done:";
    private static final String MESSAGE_TASK_UNMARKED = "OK, I've marked this task as not done yet:";
    private static final String MESSAGE_TASK_DELETED = "Noted. I've removed this task:";
    private static final String MESSAGE_TASK_ADDED = "Got it. I've added this task:";
    private static final String MESSAGE_TASK_COUNT = "Now you have %d tasks in the list.";
    private static final String MESSAGE_LIST_HEADER = "Here are the tasks in your list:";
    private static final String MESSAGE_FIND_HEADER = "Here are the matching tasks in your list:";
    private static final String MESSAGE_NO_MATCHES = "No matching tasks found.";
    private static final String MESSAGE_TASKS_SORTED = "Tasks have been sorted alphabetically by description.";

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
        assert fullCommand != null : "Command cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        String[] parts = fullCommand.split(COMMAND_SEPARATOR, COMMAND_PART_LIMIT);
        assert parts.length > 0 : "Command parts should have at least one element";
        Command action = Command.valueOf(parts[0].toUpperCase());
        assert action != null : "Command should be valid after parsing";

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
        case SORT:
            sortTasks(tasks, ui, storage);
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
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        
        ui.showMessage(MESSAGE_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            assert task != null : "Task at index " + i + " should not be null";
            ui.showMessage(tasks.formatTaskForDisplay(i, task));
        }
    }

    /**
     * Marks a specified task as completed.
     */
    private static void markTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        validateCommandParts(parts, ERROR_MARK_TASK);
        int taskNumber = parseTaskNumber(parts[1]);
        Task task = tasks.getTask(taskNumber);
        assert task != null : "Retrieved task should not be null";
        
        task.markAsDone();
        assert task.getStatusIcon().equals("X") : "Task should show as done after marking";
        
        showTaskOperationResult(ui, MESSAGE_TASK_MARKED, task);
        storage.save(tasks.getTasks());
    }

    /**
     * Marks a specified task as not completed.
     */
    private static void unmarkTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        validateCommandParts(parts, ERROR_UNMARK_TASK);
        int taskNumber = parseTaskNumber(parts[1]);
        Task task = tasks.getTask(taskNumber);
        assert task != null : "Retrieved task should not be null";
        
        task.markAsNotDone();
        assert task.getStatusIcon().equals(" ") : "Task should show as not done after unmarking";
        
        showTaskOperationResult(ui, MESSAGE_TASK_UNMARKED, task);
        storage.save(tasks.getTasks());
    }

    /**
     * Deletes a specified task from the task list.
     */
    private static void deleteTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        validateCommandParts(parts, ERROR_DELETE_TASK);
        int taskNumber = parseTaskNumber(parts[1]);
        
        int initialSize = tasks.size();
        assert taskNumber >= 0 : "Task number should be non-negative after conversion";
        assert taskNumber < initialSize : "Task number should be within valid range";
        
        Task removedTask = tasks.deleteTask(taskNumber);
        assert removedTask != null : "Removed task should not be null";
        assert tasks.size() == initialSize - 1 : "Task list should be smaller after deletion";
        
        showTaskOperationResult(ui, MESSAGE_TASK_DELETED, removedTask);
        ui.showMessage(String.format(MESSAGE_TASK_COUNT, tasks.size()));
        storage.save(tasks.getTasks());
    }

    /**
     * Adds a new todo task to the task list.
     */
    private static void addTodo(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        validateCommandParts(parts, ERROR_TODO_EMPTY);
        
        int initialSize = tasks.size();
        Task newTodo = new Todo(parts[1]);
        assert newTodo != null : "Created todo should not be null";
        assert newTodo.toString().startsWith("[T]") : "Todo should have correct type indicator";
        
        tasks.addTask(newTodo);
        assert tasks.size() == initialSize + 1 : "Task list should be larger after adding";
        
        showTaskAddedResult(ui, newTodo, tasks.size());
        storage.save(tasks.getTasks());
    }

    /**
     * Adds a new deadline task to the task list.
     */
    private static void addDeadline(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        validateCommandParts(parts, ERROR_DEADLINE_EMPTY);
        String[] deadlineParts = parts[1].split(DEADLINE_SEPARATOR);
        assert deadlineParts != null : "Deadline parts should not be null after split";
        
        if (deadlineParts.length < COMMAND_PART_LIMIT) {
            throw new ChipException(ERROR_DEADLINE_FORMAT);
        }
        
        int initialSize = tasks.size();
        Task newDeadline = new Deadline(deadlineParts[0], deadlineParts[1]);
        assert newDeadline != null : "Created deadline should not be null";
        assert newDeadline.toString().startsWith("[D]") : "Deadline should have correct type indicator";
        
        tasks.addTask(newDeadline);
        assert tasks.size() == initialSize + 1 : "Task list should be larger after adding";
        
        showTaskAddedResult(ui, newDeadline, tasks.size());
        storage.save(tasks.getTasks());
    }

    /**
     * Adds a new event task to the task list.
     */
    private static void addEvent(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        validateCommandParts(parts, ERROR_EVENT_EMPTY);
        String[] eventParts = parts[1].split(EVENT_FROM_SEPARATOR);
        assert eventParts != null : "Event parts should not be null after split";
        
        if (eventParts.length < COMMAND_PART_LIMIT) {
            throw new ChipException(ERROR_EVENT_FROM_FORMAT);
        }
        String[] timeParts = eventParts[1].split(EVENT_TO_SEPARATOR);
        assert timeParts != null : "Time parts should not be null after split";
        
        if (timeParts.length < COMMAND_PART_LIMIT) {
            throw new ChipException(ERROR_EVENT_TO_FORMAT);
        }
        
        int initialSize = tasks.size();
        Task newEvent = new Event(eventParts[0], timeParts[0], timeParts[1]);
        assert newEvent != null : "Created event should not be null";
        assert newEvent.toString().startsWith("[E]") : "Event should have correct type indicator";
        
        tasks.addTask(newEvent);
        assert tasks.size() == initialSize + 1 : "Task list should be larger after adding";
        
        showTaskAddedResult(ui, newEvent, tasks.size());
        storage.save(tasks.getTasks());
    }

    /**
     * Finds and displays tasks that contain the specified keyword.
     */
    private static void findTasks(String[] parts, TaskList tasks, Ui ui) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        
        validateCommandParts(parts, ERROR_FIND_KEYWORD);
        String keyword = parts[1];
        assert keyword != null : "Keyword should not be null";
        
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        assert matchingTasks != null : "Matching tasks should not be null";
        assert matchingTasks.size() <= tasks.size() : "Matching tasks cannot exceed total tasks";
        if (matchingTasks.isEmpty()) {
            ui.showMessage(MESSAGE_NO_MATCHES);
        } else {
            ui.showMessage(MESSAGE_FIND_HEADER);
            for (int i = 0; i < matchingTasks.size(); i++) {
                Task task = matchingTasks.get(i);
                assert task != null : "Matching task should not be null";
                ui.showMessage(tasks.formatTaskForDisplay(i, task));
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
                throw new ChipException(ERROR_NEGATIVE_TASK_NUMBER);
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new ChipException(ERROR_INVALID_TASK_NUMBER);
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
    
    /**
     * Sorts tasks by description alphabetically and saves the changes.
     *
     * @param tasks the task list to sort
     * @param ui the user interface for showing messages
     * @param storage the storage component for saving tasks
     * @throws ChipException if saving fails
     */
    private static void sortTasks(TaskList tasks, Ui ui, Storage storage) throws ChipException {
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        
        tasks.sortByDescription();
        ui.showMessage(MESSAGE_TASKS_SORTED);
        storage.save(tasks.getTasks());
    }
}