package chip.task;

import java.util.ArrayList;

/**
 * Manages a list of tasks with operations to add, delete, and retrieve tasks.
 * Provides an abstraction over the underlying ArrayList for task management.
 */
public class TaskList {
    
    // Constants
    private static final String TASK_INDENT = " ";
    private static final String TASK_SEPARATOR = ".";
    
    private ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks the existing list of tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null";
        this.tasks = tasks;
        assert this.tasks != null : "Tasks should be initialized after constructor";
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "Tasks should be initialized after constructor";
        assert this.tasks.isEmpty() : "New TaskList should start empty";
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        assert task != null : "Cannot add null task to list";
        int initialSize = tasks.size();
        tasks.add(task);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding";
        assert tasks.contains(task) : "Task should be in the list after adding";
    }

    /**
     * Removes and returns a task at the specified index.
     *
     * @param index the index of the task to delete (0-based)
     * @return the deleted task
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public Task deleteTask(int index) {
        assert index >= 0 : "Index cannot be negative";
        assert index < tasks.size() : "Index cannot be greater than or equal to list size";
        
        int initialSize = tasks.size();
        Task removedTask = tasks.remove(index);
        
        assert removedTask != null : "Removed task should not be null";
        assert tasks.size() == initialSize - 1 : "Task list size should decrease by 1 after deletion";
        
        return removedTask;
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index the index of the task to retrieve (0-based)
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public Task getTask(int index) {
        assert index >= 0 : "Index cannot be negative";
        assert index < tasks.size() : "Index cannot be greater than or equal to list size";
        
        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";
        
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        int size = tasks.size();
        assert size >= 0 : "Size should never be negative";
        return size;
    }

    /**
     * Returns the underlying ArrayList of tasks.
     *
     * @return the ArrayList containing all tasks
     */
    public ArrayList<Task> getTasks() {
        assert this.tasks != null : "Tasks list should never be null";
        return this.tasks;
    }

    /**
     * Finds tasks that contain the specified keyword in their description.
     *
     * @param keyword the keyword to search for in task descriptions
     * @return ArrayList of tasks that contain the keyword (case-insensitive)
     */
    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Keyword cannot be null";
        assert !keyword.trim().isEmpty() : "Keyword cannot be empty";
        
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Task task : tasks) {
            assert task != null : "Task in list should not be null";
            if (task.toString().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }
        
        assert matchingTasks != null : "Matching tasks list should not be null";
        assert matchingTasks.size() <= tasks.size() : "Matching tasks cannot exceed total tasks";
        
        return matchingTasks;
    }
    
    /**
     * Formats a task for display with its index number.
     *
     * @param index the 0-based index of the task
     * @param task the task to format
     * @return formatted string representation of the task
     */
    public String formatTaskForDisplay(int index, Task task) {
        return TASK_INDENT + (index + 1) + TASK_SEPARATOR + task;
    }
}