package chip.task;

import chip.ChipException;

/**
 * Represents a generic task with a description and completion status.
 * This is the base class for all specific task types (Todo, Deadline, Event).
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructs a new Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
        assert this.isDone : "Task should be marked as done after calling markAsDone()";
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
        assert !this.isDone : "Task should be marked as not done after calling markAsNotDone()";
    }

    /**
     * Returns the status icon for this task.
     *
     * @return "X" if task is done, " " (space) if not done
     */
    public String getStatusIcon() {
        String icon = (isDone ? "X" : " ");
        assert icon.equals("X") || icon.equals(" ") : "Status icon should be either 'X' or ' '";
        return icon;
    }

    /**
     * Returns the string representation for saving to file.
     * Format: "status | description" where status is "1" for done, "0" for not done.
     *
     * @return the file format string representation of this task
     */
    public String toFileString() {
        String result = (isDone ? "1" : "0") + " | " + this.description;
        assert result.contains(" | ") : "File string should contain separator ' | '";
        assert result.startsWith("0 | ") || result.startsWith("1 | ") : "File string should start with status";
        return result;
    }

    /**
     * Returns the string representation for display to user.
     * Format: "[status] description" where status is "X" for done, " " for not done.
     *
     * @return the display format string representation of this task
     */
    @Override
    public String toString() {
        String result = "[" + getStatusIcon() + "] " + this.description;
        assert result.startsWith("[") && result.contains("]") : "Display string should have status in brackets";
        return result;
    }
}