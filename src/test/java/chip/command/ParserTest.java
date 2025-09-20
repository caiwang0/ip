package chip.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import chip.ChipException;
import chip.task.TaskList;
import chip.storage.Storage;
import chip.ui.Ui;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Test class for the Parser command handling functionality.
 * Tests command parsing, validation, and execution.
 */
public class ParserTest {

    private TaskList taskList;
    private Ui ui;
    private Storage storage;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        ui = new Ui();
        storage = new Storage("./test-data.txt");
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testHelpCommand() throws ChipException {
        Parser.parse("help", taskList, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("Here are the commands I understand:"));
        assertTrue(output.contains("todo <description>"));
        assertTrue(output.contains("deadline <description>"));
        assertTrue(output.contains("list"));
        assertTrue(output.contains("mark <number>"));
    }

    @Test
    public void testListCommand() throws ChipException {
        taskList.addTask(new chip.task.Todo("Test task"));
        Parser.parse("list", taskList, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("Here are the tasks in your list:"));
        assertTrue(output.contains("Test task"));
    }

    @Test
    public void testTodoCommand() throws ChipException {
        Parser.parse("todo Read a book", taskList, ui, storage);
        assertEquals(1, taskList.size());
        assertTrue(taskList.getTask(0).toString().contains("Read a book"));
    }

    @Test
    public void testDeadlineCommand() throws ChipException {
        Parser.parse("deadline Submit report /by 2024-12-31 1800", taskList, ui, storage);
        assertEquals(1, taskList.size());
        assertTrue(taskList.getTask(0).toString().contains("Submit report"));
        assertTrue(taskList.getTask(0).toString().contains("by:"));
    }

    @Test
    public void testEventCommand() throws ChipException {
        Parser.parse("event Team meeting /from 2024-12-25 1400 /to 2024-12-25 1600", taskList, ui, storage);
        assertEquals(1, taskList.size());
        assertTrue(taskList.getTask(0).toString().contains("Team meeting"));
        assertTrue(taskList.getTask(0).toString().contains("from:"));
        assertTrue(taskList.getTask(0).toString().contains("to:"));
    }

    @Test
    public void testMarkCommand() throws ChipException {
        taskList.addTask(new chip.task.Todo("Test task"));
        Parser.parse("mark 1", taskList, ui, storage);
        assertTrue(taskList.getTask(0).getStatusIcon().equals("X"));
    }

    @Test
    public void testUnmarkCommand() throws ChipException {
        chip.task.Todo todo = new chip.task.Todo("Test task");
        todo.markAsDone();
        taskList.addTask(todo);
        Parser.parse("unmark 1", taskList, ui, storage);
        assertTrue(taskList.getTask(0).getStatusIcon().equals(" "));
    }

    @Test
    public void testDeleteCommand() throws ChipException {
        taskList.addTask(new chip.task.Todo("Task 1"));
        taskList.addTask(new chip.task.Todo("Task 2"));
        Parser.parse("delete 1", taskList, ui, storage);
        assertEquals(1, taskList.size());
        assertTrue(taskList.getTask(0).toString().contains("Task 2"));
    }

    @Test
    public void testFindCommand() throws ChipException {
        taskList.addTask(new chip.task.Todo("Read book"));
        taskList.addTask(new chip.task.Todo("Write report"));
        Parser.parse("find read", taskList, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("Here are the matching tasks"));
        assertTrue(output.contains("Read book"));
    }

    @Test
    public void testSortCommand() throws ChipException {
        taskList.addTask(new chip.task.Todo("Zebra task"));
        taskList.addTask(new chip.task.Todo("Apple task"));
        Parser.parse("sort", taskList, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("Tasks have been sorted"));
        assertEquals("Apple task", taskList.getTask(0).toString().substring(6).trim());
    }

    @Test
    public void testInvalidCommand() {
        assertThrows(ChipException.class, () -> {
            Parser.parse("invalid", taskList, ui, storage);
        });
    }

    @Test
    public void testEmptyTodoCommand() {
        assertThrows(ChipException.class, () -> {
            Parser.parse("todo", taskList, ui, storage);
        });
    }

    @Test
    public void testInvalidMarkCommand() {
        assertThrows(ChipException.class, () -> {
            Parser.parse("mark", taskList, ui, storage);
        });
    }

    @Test
    public void testInvalidTaskNumber() {
        taskList.addTask(new chip.task.Todo("Test task"));
        assertThrows(ChipException.class, () -> {
            Parser.parse("mark 0", taskList, ui, storage);
        });
    }

    @Test
    public void testDeadlineWithoutBy() {
        assertThrows(ChipException.class, () -> {
            Parser.parse("deadline Submit report", taskList, ui, storage);
        });
    }

    @Test
    public void testEventWithoutFrom() {
        assertThrows(ChipException.class, () -> {
            Parser.parse("event Team meeting", taskList, ui, storage);
        });
    }

    @Test
    public void testEventWithoutTo() {
        assertThrows(ChipException.class, () -> {
            Parser.parse("event Team meeting /from 2024-12-25 1400", taskList, ui, storage);
        });
    }
}
