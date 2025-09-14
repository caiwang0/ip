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
        
        String[] parts = fullCommand.split(" ", 2);
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
        
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            assert task != null : "Task at index " + i + " should not be null";
            ui.showMessage(" " + (i + 1) + "." + task);
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
        
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to mark.");
        }
        
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        assert taskNumber >= 0 : "Task number should be non-negative after conversion";
        
        Task task = tasks.getTask(taskNumber);
        assert task != null : "Retrieved task should not be null";
        
        task.markAsDone();
        assert task.getStatusIcon().equals("X") : "Task should show as done after marking";
        
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("   " + task);
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
        
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to unmark.");
        }
        
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        assert taskNumber >= 0 : "Task number should be non-negative after conversion";
        
        Task task = tasks.getTask(taskNumber);
        assert task != null : "Retrieved task should not be null";
        
        task.markAsNotDone();
        assert task.getStatusIcon().equals(" ") : "Task should show as not done after unmarking";
        
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("   " + task);
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
        
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to delete.");
        }
        
        int initialSize = tasks.size();
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        assert taskNumber >= 0 : "Task number should be non-negative after conversion";
        assert taskNumber < initialSize : "Task number should be within valid range";
        
        Task removedTask = tasks.deleteTask(taskNumber);
        assert removedTask != null : "Removed task should not be null";
        assert tasks.size() == initialSize - 1 : "Task list should be smaller after deletion";
        
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("   " + removedTask);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
        
        if (parts.length < 2) {
            throw new ChipException("The description of a todo cannot be empty.");
        }
        
        int initialSize = tasks.size();
        Task newTodo = new Todo(parts[1]);
        assert newTodo != null : "Created todo should not be null";
        assert newTodo.toString().startsWith("[T]") : "Todo should have correct type indicator";
        
        tasks.addTask(newTodo);
        assert tasks.size() == initialSize + 1 : "Task list should be larger after adding";
        
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newTodo);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
        
        if (parts.length < 2) {
            throw new ChipException("The description of a deadline cannot be empty.");
        }
        
        String[] deadlineParts = parts[1].split(" /by ");
        assert deadlineParts != null : "Deadline parts should not be null after split";
        
        if (deadlineParts.length < 2) {
            throw new ChipException("Please specify the deadline time using /by.");
        }
        
        int initialSize = tasks.size();
        Task newDeadline = new Deadline(deadlineParts[0], deadlineParts[1]);
        assert newDeadline != null : "Created deadline should not be null";
        assert newDeadline.toString().startsWith("[D]") : "Deadline should have correct type indicator";
        
        tasks.addTask(newDeadline);
        assert tasks.size() == initialSize + 1 : "Task list should be larger after adding";
        
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newDeadline);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
        
        if (parts.length < 2) {
            throw new ChipException("The description of an event cannot be empty.");
        }
        
        String[] eventParts = parts[1].split(" /from ");
        assert eventParts != null : "Event parts should not be null after split";
        
        if (eventParts.length < 2) {
            throw new ChipException("Please specify the event start time using /from.");
        }
        
        String[] timeParts = eventParts[1].split(" /to ");
        assert timeParts != null : "Time parts should not be null after split";
        
        if (timeParts.length < 2) {
            throw new ChipException("Please specify the event end time using /to.");
        }
        
        int initialSize = tasks.size();
        Task newEvent = new Event(eventParts[0], timeParts[0], timeParts[1]);
        assert newEvent != null : "Created event should not be null";
        assert newEvent.toString().startsWith("[E]") : "Event should have correct type indicator";
        
        tasks.addTask(newEvent);
        assert tasks.size() == initialSize + 1 : "Task list should be larger after adding";
        
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newEvent);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    /**
     * Finds and displays tasks that contain the specified keyword.
     */
    private static void findTasks(String[] parts, TaskList tasks, Ui ui) throws ChipException {
        assert parts != null : "Command parts cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        
        if (parts.length < 2) {
            throw new ChipException("Please specify a keyword to search for.");
        }

        String keyword = parts[1];
        assert keyword != null : "Keyword should not be null";
        
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        assert matchingTasks != null : "Matching tasks should not be null";
        assert matchingTasks.size() <= tasks.size() : "Matching tasks cannot exceed total tasks";

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No matching tasks found.");
        } else {
            ui.showMessage("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                Task task = matchingTasks.get(i);
                assert task != null : "Matching task should not be null";
                ui.showMessage(" " + (i + 1) + "." + task);
            }
        }
    }
}