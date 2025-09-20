package chip;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import chip.task.Task;
import chip.task.Todo;
import chip.task.Deadline;
import chip.task.Event;
import chip.task.TaskList;
import chip.ChipException;

/**
 * Test class for the Chip task management application.
 * Tests the core functionality of tasks, task lists, and basic operations.
 */
public class ChipTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void testTodoCreation() {
        Todo todo = new Todo("Read a book");
        assertEquals("Read a book", todo.toString().substring(6).trim()); // Skip "[ ] " prefix and trim
        assertFalse(todo.getStatusIcon().equals("X"));
    }

    @Test
    public void testTodoMarkAsDone() {
        Todo todo = new Todo("Read a book");
        todo.markAsDone();
        assertTrue(todo.getStatusIcon().equals("X"));
    }

    @Test
    public void testTodoMarkAsNotDone() {
        Todo todo = new Todo("Read a book");
        todo.markAsDone();
        todo.markAsNotDone();
        assertTrue(todo.getStatusIcon().equals(" "));
    }

    @Test
    public void testDeadlineCreation() throws ChipException {
        Deadline deadline = new Deadline("Submit report", "2024-12-31 1800");
        assertTrue(deadline.toString().contains("Submit report"));
        assertTrue(deadline.toString().contains("by:"));
    }

    @Test
    public void testEventCreation() throws ChipException {
        Event event = new Event("Team meeting", "2024-12-25 1400", "2024-12-25 1600");
        assertTrue(event.toString().contains("Team meeting"));
        assertTrue(event.toString().contains("from:"));
        assertTrue(event.toString().contains("to:"));
    }

    @Test
    public void testTaskListAddTask() {
        Todo todo = new Todo("Test task");
        taskList.addTask(todo);
        assertEquals(1, taskList.size());
        assertEquals(todo, taskList.getTask(0));
    }

    @Test
    public void testTaskListDeleteTask() {
        Todo todo1 = new Todo("Task 1");
        Todo todo2 = new Todo("Task 2");
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        
        Task deleted = taskList.deleteTask(0);
        assertEquals(todo1, deleted);
        assertEquals(1, taskList.size());
        assertEquals(todo2, taskList.getTask(0));
    }

    @Test
    public void testTaskListFindTasks() {
        Todo todo1 = new Todo("Read book");
        Todo todo2 = new Todo("Write report");
        Todo todo3 = new Todo("Read email");
        
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);
        
        var foundTasks = taskList.findTasks("read");
        assertEquals(2, foundTasks.size());
        assertTrue(foundTasks.contains(todo1));
        assertTrue(foundTasks.contains(todo3));
    }

    @Test
    public void testTaskListSortByDescription() {
        Todo todo1 = new Todo("Zebra task");
        Todo todo2 = new Todo("Apple task");
        Todo todo3 = new Todo("Banana task");
        
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);
        
        taskList.sortByDescription();
        
        assertEquals("Apple task", taskList.getTask(0).toString().substring(6).trim());
        assertEquals("Banana task", taskList.getTask(1).toString().substring(6).trim());
        assertEquals("Zebra task", taskList.getTask(2).toString().substring(6).trim());
    }

    @Test
    public void testTaskToFileString() {
        Todo todo = new Todo("Test task");
        String fileString = todo.toFileString();
        assertTrue(fileString.contains("T | 0 | Test task"));
        
        todo.markAsDone();
        fileString = todo.toFileString();
        assertTrue(fileString.contains("T | 1 | Test task"));
    }

    @Test
    public void testEmptyTaskList() {
        assertEquals(0, taskList.size());
        assertTrue(taskList.findTasks("anything").isEmpty());
    }

    @Test
    public void testTaskListFormatTaskForDisplay() {
        Todo todo = new Todo("Test task");
        taskList.addTask(todo);
        
        String formatted = taskList.formatTaskForDisplay(0, todo);
        assertTrue(formatted.contains("1."));
        assertTrue(formatted.contains("Test task"));
    }
}
