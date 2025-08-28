import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDateTime by;

    public Deadline(String description, String by) throws ChipException {
        super(description);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            this.by = LocalDateTime.parse(by, formatter);
        } catch (DateTimeParseException e) {
            throw new ChipException("Please use the date/time format yyyy-MM-dd HHmm.");
        }
    }

    @Override
    public String toFileString() {
        String formattedDate = this.by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "D | " + super.toFileString() + " | " + formattedDate;
    }

    @Override
    public String toString() {
        String formattedDate = this.by.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}