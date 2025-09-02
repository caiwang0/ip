package chip;

import chip.task.Task;
import chip.task.TaskList;
import chip.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {
    private TaskList taskList;
    private Task sampleTask;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        sampleTask = new Todo("Sample task");
    }

    @Test
    public void addTask_singleTask_sizeIncreasesCorrectly() {
        taskList.addTask(sampleTask);
        assertEquals(1, taskList.size());
    }

    @Test
    public void addTask_multipleTasks_sizeIncreasesCorrectly() {
        taskList.addTask(sampleTask);
        taskList.addTask(new Todo("Another task"));
        taskList.addTask(new Todo("Third task"));
        assertEquals(3, taskList.size());
    }

    @Test
    public void getTask_validIndex_returnsCorrectTask() {
        taskList.addTask(sampleTask);
        Task retrievedTask = taskList.getTask(0);
        assertEquals(sampleTask, retrievedTask);
    }

    @Test
    public void getTask_invalidIndex_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTask(0); // Empty list
        });

        taskList.addTask(sampleTask);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTask(1); // Index out of bounds
        });
    }

    @Test
    public void deleteTask_validIndex_removesTaskAndReturnsIt() {
        taskList.addTask(sampleTask);
        Task deletedTask = taskList.deleteTask(0);
        assertEquals(sampleTask, deletedTask);
        assertEquals(0, taskList.size());
    }

    @Test
    public void deleteTask_invalidIndex_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(0); // Empty list
        });
    }

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, taskList.size());
    }

    @Test
    public void size_afterAddingTasks_returnsCorrectSize() {
        taskList.addTask(new Todo("Task 1"));
        taskList.addTask(new Todo("Task 2"));
        assertEquals(2, taskList.size());
    }
}