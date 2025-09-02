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
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon for this task.
     *
     * @return "X" if task is done, " " (space) if not done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the string representation for saving to file.
     * Format: "status | description" where status is "1" for done, "0" for not done.
     *
     * @return the file format string representation of this task
     */
    public String toFileString() {
        return (isDone ? "1" : "0") + " | " + this.description;
    }

    /**
     * Returns the string representation for display to user.
     * Format: "[status] description" where status is "X" for done, " " for not done.
     *
     * @return the display format string representation of this task
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}