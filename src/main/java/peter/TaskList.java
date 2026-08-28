package peter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the current list of tasks and the operations that can be
 * performed on it. Contains no console I/O and no parsing logic.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list pre-populated with tasks, typically loaded from storage.
     *
     * @param loadedTasks the tasks to start with
     */
    public TaskList(List<Task> loadedTasks) {
        this.tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param index the zero-based task index
     * @return the removed task
     * @throws PeterException if the index is out of range
     */
    public Task delete(int index) throws PeterException {
        validateIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param index the zero-based task index
     * @return the task at that index
     * @throws PeterException if the index is out of range
     */
    public Task get(int index) throws PeterException {
        validateIndex(index);
        return tasks.get(index);
    }

    /**
     * Marks the task at the given zero-based index as completed.
     *
     * @param index the zero-based task index
     * @return the marked task
     * @throws PeterException if the index is out of range
     */
    public Task mark(int index) throws PeterException {
        Task task = get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the given zero-based index as incomplete.
     *
     * @param index the zero-based task index
     * @return the unmarked task
     * @throws PeterException if the index is out of range
     */
    public Task unmark(int index) throws PeterException {
        Task task = get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns all tasks in the list.
     *
     * @return the underlying list of tasks
     */
    public ArrayList<Task> getAll() {
        return tasks;
    }

    /**
     * Returns the deadlines due, and events starting, on the given date.
     *
     * @param date the date to match against
     * @return the tasks occurring on that date
     */
    public List<Task> getTasksOnDate(LocalDate date) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline && ((Deadline) task).getBy().toLocalDate().equals(date)) {
                matchingTasks.add(task);
            } else if (task instanceof Event && ((Event) task).getFrom().toLocalDate().equals(date)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Checks that a zero-based index refers to an existing task.
     *
     * @param index the zero-based task index
     * @throws PeterException if the index is out of range
     */
    private void validateIndex(int index) throws PeterException {
        if (index < 0 || index >= tasks.size()) {
            throw new PeterException("that task doesn't exist buddy.");
        }
    }
}