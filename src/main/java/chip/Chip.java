package chip;

import java.util.Scanner;
import java.util.ArrayList;
import chip.command.*;
import chip.task.*;
import chip.storage.*;
import chip.ui.*;

public class Chip {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Chip(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ChipException e) {
            ui.showError("Data file not found. Starting with an empty task list.");
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                // Check for bye command separately since it doesn't modify tasks
                if (fullCommand.trim().equalsIgnoreCase("bye")) {
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                }

                // Parse and execute command
                executeCommand(fullCommand);

            } catch (ChipException e) {
                ui.showError(e.getMessage());
            } catch (IllegalArgumentException e) {
                ui.showError("I'm sorry, there is no such action.");
            } catch (Exception e) {
                ui.showError("An unexpected error occurred. Please check your command.");
            } finally {
                ui.showLine();
            }
        }
    }

    private void executeCommand(String fullCommand) throws ChipException {
        String[] parts = fullCommand.split(" ", 2);
        Command action = Command.valueOf(parts[0].toUpperCase());

        switch (action) {
            case LIST:
                showTaskList();
                break;
            case MARK:
                markTask(parts);
                break;
            case UNMARK:
                unmarkTask(parts);
                break;
            case DELETE:
                deleteTask(parts);
                break;
            case TODO:
                addTodo(parts);
                break;
            case DEADLINE:
                addDeadline(parts);
                break;
            case EVENT:
                addEvent(parts);
                break;
        }
    }

    private void showTaskList() {
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    private void markTask(String[] parts) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to mark.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("   " + task);
        saveTasksToFile();
    }

    private void unmarkTask(String[] parts) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to unmark.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("   " + task);
        saveTasksToFile();
    }

    private void deleteTask(String[] parts) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to delete.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task removedTask = tasks.deleteTask(taskNumber);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("   " + removedTask);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        saveTasksToFile();
    }

    private void addTodo(String[] parts) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("The description of a todo cannot be empty.");
        }
        Task newTodo = new Todo(parts[1]);
        tasks.addTask(newTodo);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newTodo);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        saveTasksToFile();
    }

    private void addDeadline(String[] parts) throws ChipException {
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
        saveTasksToFile();
    }

    private void addEvent(String[] parts) throws ChipException {
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
        saveTasksToFile();
    }

    private void saveTasksToFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (ChipException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Chip("./data/chip.txt").run();
    }
}