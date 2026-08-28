package peter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TaskList}.
 */
public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void add_singleTask_increasesSize() {
        taskList.add(new Todo("read book"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void get_validIndex_returnsCorrectTask() throws PeterException {
        Todo todo = new Todo("read book");
        taskList.add(todo);
        assertEquals(todo, taskList.get(0));
    }

    @Test
    public void get_negativeIndex_throwsPeterException() {
        taskList.add(new Todo("read book"));
        assertThrows(PeterException.class, () -> taskList.get(-1));
    }

    @Test
    public void get_indexEqualToSize_throwsPeterException() {
        taskList.add(new Todo("read book"));
        // valid indices are 0..size-1, so index == size is out of range
        assertThrows(PeterException.class, () -> taskList.get(1));
    }

    @Test
    public void get_emptyList_throwsPeterException() {
        assertThrows(PeterException.class, () -> taskList.get(0));
    }

    @Test
    public void delete_validIndex_removesAndReturnsTask() throws PeterException {
        Todo todo = new Todo("read book");
        taskList.add(todo);
        taskList.add(new Todo("return book"));

        Task removed = taskList.delete(0);

        assertEquals(todo, removed);
        assertEquals(1, taskList.size());
        assertEquals("return book", taskList.get(0).getDescription());
    }

    @Test
    public void delete_invalidIndex_throwsPeterExceptionAndLeavesListUnchanged() {
        taskList.add(new Todo("read book"));

        assertThrows(PeterException.class, () -> taskList.delete(5));
        // list should be untouched since the delete never happened
        assertEquals(1, taskList.size());
    }

    @Test
    public void mark_validIndex_marksTaskDone() throws PeterException {
        taskList.add(new Todo("read book"));

        Task marked = taskList.mark(0);

        assertTrue(marked.toString().startsWith("[T][X]"));
    }

    @Test
    public void unmark_validIndex_marksTaskNotDone() throws PeterException {
        taskList.add(new Todo("read book"));
        taskList.mark(0);

        Task unmarked = taskList.unmark(0);

        assertTrue(unmarked.toString().startsWith("[T][ ]"));
    }

    @Test
    public void mark_invalidIndex_throwsPeterException() {
        assertThrows(PeterException.class, () -> taskList.mark(0));
    }

    @Test
    public void getTasksOnDate_matchingDeadlineAndEvent_returnsBoth() {
        LocalDate targetDate = LocalDate.of(2019, 12, 2);
        Deadline matchingDeadline = new Deadline("return book",
                LocalDateTime.of(2019, 12, 2, 18, 0));
        Event matchingEvent = new Event("project meeting",
                LocalDateTime.of(2019, 12, 2, 14, 0),
                LocalDateTime.of(2019, 12, 2, 16, 0));
        Deadline nonMatchingDeadline = new Deadline("submit report",
                LocalDateTime.of(2019, 12, 3, 18, 0));

        taskList.add(matchingDeadline);
        taskList.add(matchingEvent);
        taskList.add(nonMatchingDeadline);
        taskList.add(new Todo("unrelated todo"));

        List<Task> tasksOnDate = taskList.getTasksOnDate(targetDate);

        assertEquals(2, tasksOnDate.size());
        assertTrue(tasksOnDate.contains(matchingDeadline));
        assertTrue(tasksOnDate.contains(matchingEvent));
        assertFalse(tasksOnDate.contains(nonMatchingDeadline));
    }

    @Test
    public void getTasksOnDate_noMatches_returnsEmptyList() {
        taskList.add(new Deadline("return book", LocalDateTime.of(2019, 12, 2, 18, 0)));

        List<Task> tasksOnDate = taskList.getTasksOnDate(LocalDate.of(2020, 1, 1));

        assertTrue(tasksOnDate.isEmpty());
    }

    @Test
    public void constructor_withLoadedTasks_preservesAllTasks() {
        Todo loadedTodo = new Todo("read book");
        TaskList loadedList = new TaskList(List.of(loadedTodo));

        assertEquals(1, loadedList.size());
        assertEquals(loadedTodo, loadedList.getAll().get(0));
    }
}
