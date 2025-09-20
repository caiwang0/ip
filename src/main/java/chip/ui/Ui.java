package chip.ui;

import java.util.Scanner;

/**
 * Handles all user interface operations for the Chip application.
 * Manages input reading and output display to provide a consistent user experience.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a new Ui instance and initializes the input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Chip");
        System.out.println(" What can I do for you?");
        System.out.println(" Type 'help' to see available commands.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Reads a command line from the user.
     *
     * @return the command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a horizontal line separator for visual formatting.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an error message with the "OOPS!!!" prefix.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
        System.out.println(" Type 'help' to see available commands.");
    }

    /**
     * Displays a regular message to the user.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showGoodbye() {
        System.out.println(" Bye. Hope to see you again soon!");
    }
}