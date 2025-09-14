package chip;

import chip.command.Parser;
import chip.storage.Storage;
import chip.task.TaskList;
import chip.ui.Ui;

/**
 * Main class for the Chip task management application.
 * Handles initialization and coordination between UI, storage, and task management components.
 */
public class Chip {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a new Chip instance with the specified file path for data storage.
     * Initializes UI, storage, and loads existing tasks from file.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public Chip(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path should not be null or empty";
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ChipException e) {
            ui.showError("Data file not found. Starting with an empty task list.");
            tasks = new TaskList();
        }
        assert ui != null : "UI should be initialized";
        assert storage != null : "Storage should be initialized";
        assert tasks != null : "TaskList should be initialized";
    }

    /**
     * Generates a response for the user's chat message for GUI usage.
     *
     * @param input the user's input command
     * @return the response string to display in GUI
     */
    public String getResponse(String input) {
        assert input != null : "Input should not be null";
        try {
            if (input.trim().equalsIgnoreCase("bye")) {
                return "Bye. Hope to see you again soon!";
            }

            StringBuilder response = new StringBuilder();

            Ui mockUi = new Ui() {
                @Override
                public void showMessage(String message) {
                    response.append(message).append("\n");
                }

                @Override
                public void showError(String message) {
                    response.append("OOPS!!! ").append(message).append("\n");
                }
            };

            Parser.parse(input, tasks, mockUi, storage);
            String result = response.toString().trim();
            assert result != null : "Response should not be null";
            return result;

        } catch (ChipException e) {
            return "OOPS!!! " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "OOPS!!! I'm sorry, there is no such action.";
        } catch (Exception e) {
            return "OOPS!!! An unexpected error occurred. Please check your command.";
        }
    }

    /**
     * Starts the main application loop.
     * Displays welcome message and continuously processes user commands until exit.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                if (fullCommand.trim().equalsIgnoreCase("bye")) {
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                }

                Parser.parse(fullCommand, tasks, ui, storage);

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

    /**
     * Entry point for the Chip application.
     * Creates a new Chip instance and starts the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Chip("./data/chip.txt").run();
    }
}