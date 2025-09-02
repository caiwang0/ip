package chip;

import chip.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TodoTest {
    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo("Buy groceries");
    }

    @Test
    public void constructor_validDescription_createsUndoneTask() {
        assertEquals(" ", todo.getStatusIcon());
        assertTrue(todo.toString().contains("Buy groceries"));
        assertTrue(todo.toString().contains("[T][ ]"));
    }

    @Test
    public void markAsDone_undoneTask_marksTaskAsDone() {
        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());
        assertTrue(todo.toString().contains("[T][X]"));
    }

    @Test
    public void markAsNotDone_doneTask_marksTaskAsUndone() {
        todo.markAsDone(); // First mark as done
        todo.markAsNotDone(); // Then mark as not done
        assertEquals(" ", todo.getStatusIcon());
        assertTrue(todo.toString().contains("[T][ ]"));
    }

    @Test
    public void toString_undoneTask_correctFormat() {
        String expected = "[T][ ] Buy groceries";
        assertEquals(expected, todo.toString());
    }

    @Test
    public void toString_doneTask_correctFormat() {
        todo.markAsDone();
        String expected = "[T][X] Buy groceries";
        assertEquals(expected, todo.toString());
    }

    @Test
    public void toFileString_undoneTask_correctFormat() {
        String result = todo.toFileString();
        assertEquals("T | 0 | Buy groceries", result);
    }

    @Test
    public void toFileString_doneTask_correctFormat() {
        todo.markAsDone();
        String result = todo.toFileString();
        assertEquals("T | 1 | Buy groceries", result);
    }

    @Test
    public void emptyDescription_createsValidTodo() {
        Todo emptyTodo = new Todo("");
        assertEquals("[T][ ] ", emptyTodo.toString());
        assertEquals("T | 0 | ", emptyTodo.toFileString());
    }
}