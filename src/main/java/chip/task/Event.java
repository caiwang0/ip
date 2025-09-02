package chip.task;

import chip.ChipException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

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

    @Override
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        String formattedFrom = this.from.format(formatter);
        String formattedTo = this.to.format(formatter);
        return "E | " + super.toFileString() + " | " + formattedFrom + " | " + formattedTo;
    }

    @Override
    public String toString() {
        String formattedFrom = this.from.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
        String formattedTo = this.to.format(DateTimeFormatter.ofPattern("h:mma")); // Example: just show time for the 'to' part
        return "[E]" + super.toString() + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }
}