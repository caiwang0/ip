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
        String[] parts = fullCommand.split(" ", 2);
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
        ui.showMessage("Here are the tasks in your list:");
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
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to mark.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("   " + task);
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
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to unmark.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("   " + task);
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
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to delete.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task removedTask = tasks.deleteTask(taskNumber);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("   " + removedTask);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
        if (parts.length < 2) {
            throw new ChipException("The description of a todo cannot be empty.");
        }
        Task newTodo = new Todo(parts[1]);
        tasks.addTask(newTodo);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newTodo);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
        if (parts.length < 2) {
            throw new ChipException("The description of an event cannot be empty.");
        }
        String[] eventParts = parts[1].split(" /from ");
        if (eventParts.length < 2) {
            throw new ChipException("Please specify the event start time using /from.");
        }
        String[] timeParts = eventParts[1].split(" /to ");
        if (timeParts.length < 2) {
            throw new ChipException("Please specify the event end time using /to.");
        }
        Task newEvent = new Event(eventParts[0], timeParts[0], timeParts[1]);
        tasks.addTask(newEvent);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newEvent);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
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
        if (parts.length < 2) {
            throw new ChipException("Please specify a keyword to search for.");
        }

        String keyword = parts[1];
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No matching tasks found.");
        } else {
            ui.showMessage("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }
}