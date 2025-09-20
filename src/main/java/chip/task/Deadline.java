package chip.task;

import chip.ChipException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 * Extends the base Task class to include deadline functionality and formatting.
 */
public class Deadline extends Task {
    private LocalDateTime by;

    /**
     * Constructs a new Deadline task with the given description and deadline.
     *
     * @param description the description of the deadline task
     * @param by the deadline date/time in format "yyyy-MM-dd HHmm"
     * @throws ChipException if the date/time format is invalid
     */
    public Deadline(String description, String by) throws ChipException {
        super(description);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            this.by = LocalDateTime.parse(by, formatter);
        } catch (DateTimeParseException e) {
            throw new ChipException("Please use the date/time format yyyy-MM-dd HHmm.");
        }
    }

    /**
     * AI-Enhanced: Returns the file format string for this deadline task.
     * Format: "D | status | priority | description | deadline"
     *
     * @return the file format string representation
     */
    @Override
    public String toFileString() {
        String formattedDate = this.by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "D | " + super.toFileString() + " | " + formattedDate;
    }

    /**
     * AI-Enhanced: Returns the display format string for this deadline task.
     * Format: "[D][status] [priority] description (by: formatted_date)"
     *
     * @return the display format string representation
     */
    @Override
    public String toString() {
        String formattedDate = this.by.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}