package chip;

/**
 * Custom exception class for the Chip application.
 * Used to handle application-specific errors and provide meaningful error messages to users.
 */
public class ChipException extends Exception {

    /**
     * Constructs a new ChipException with the specified error message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public ChipException(String message) {
        super(message);
    }
}