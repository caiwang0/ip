package chip.task;

import chip.ChipException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that occurs during a specific time period.
 * Extends the base Task class to include start and end time functionality.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructs a new Event task with the given description, start time, and end time.
     *
     * @param description the description of the event
     * @param from the start date/time in format "yyyy-MM-dd HHmm"
     * @param to the end date/time in format "yyyy-MM-dd HHmm"
     * @throws ChipException if the date/time format is invalid
     */
    public Event(String description, String from, String to) throws ChipException {
        super(description);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            this.from = LocalDateTime.parse(from, formatter);
            this.to = LocalDateTime.parse(to, formatter);
        } catch (DateTimeParseException e) {
            throw new ChipException("Oops! Please use the date/time format yyyy-MM-dd HHmm for from/to dates.");
        }
    }

    /**
     * AI-Enhanced: Returns the file format string for this event task.
     * Format: "E | status | priority | description | start_time | end_time"
     *
     * @return the file format string representation
     */
    @Override
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        String formattedFrom = this.from.format(formatter);
        String formattedTo = this.to.format(formatter);
        return "E | " + super.toFileString() + " | " + formattedFrom + " | " + formattedTo;
    }

    /**
     * AI-Enhanced: Returns the display format string for this event task.
     * Format: "[E][status] [priority] description (from: formatted_start_date to: formatted_end_time)"
     *
     * @return the display format string representation
     */
    @Override
    public String toString() {
        String formattedFrom = this.from.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
        String formattedTo = this.to.format(DateTimeFormatter.ofPattern("h:mma"));
        return "[E]" + super.toString() + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }
}