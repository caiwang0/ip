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
    
    // Constants
    private static final String DEFAULT_FILE_PATH = "./data/chip.txt";
    private static final String BYE_COMMAND = "bye";
    private static final String GOODBYE_MESSAGE = "Bye. Hope to see you again soon!";
    private static final String ERROR_PREFIX = "OOPS!!! ";
    private static final String ERROR_NO_ACTION = "I'm sorry, there is no such action.";
    private static final String ERROR_UNEXPECTED = "An unexpected error occurred. Please check your command.";
    private static final String ERROR_FILE_NOT_FOUND = "Data file not found. Starting with an empty task list.";
    private static final String NEWLINE = "\n";

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
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ChipException e) {
            ui.showError(ERROR_FILE_NOT_FOUND);
            tasks = new TaskList();
        }
    }

    /**
     * Generates a response for the user's chat message for GUI usage.
     *
     * @param input the user's input command
     * @return the response string to display in GUI
     */
    public String getResponse(String input) {
        try {
            if (input.trim().equalsIgnoreCase(BYE_COMMAND)) {
                return GOODBYE_MESSAGE;
            }

            StringBuilder response = new StringBuilder();

            Ui mockUi = new Ui() {
                @Override
                public void showMessage(String message) {
                    response.append(message).append(NEWLINE);
                }

                @Override
                public void showError(String message) {
                    response.append(ERROR_PREFIX).append(message).append(NEWLINE);
                }
            };

            Parser.parse(input, tasks, mockUi, storage);

            return response.toString().trim();

        } catch (ChipException e) {
            return ERROR_PREFIX + e.getMessage();
        } catch (IllegalArgumentException e) {
            return ERROR_PREFIX + ERROR_NO_ACTION;
        } catch (Exception e) {
            return ERROR_PREFIX + ERROR_UNEXPECTED;
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

                if (fullCommand.trim().equalsIgnoreCase(BYE_COMMAND)) {
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                }

                Parser.parse(fullCommand, tasks, ui, storage);

            } catch (ChipException e) {
                ui.showError(e.getMessage());
            } catch (IllegalArgumentException e) {
                ui.showError(ERROR_NO_ACTION);
            } catch (Exception e) {
                ui.showError(ERROR_UNEXPECTED);
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
        new Chip(DEFAULT_FILE_PATH).run();
    }
}