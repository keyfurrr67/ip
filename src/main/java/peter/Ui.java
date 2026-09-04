package peter;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Builds Peter's response text for user-facing events, and reads console
 * input for the command-line entry point. Contains no formatting for visual
 * borders, so the same text can be shown in the console or in the GUI.
 */
public class Ui {
    private static final String BANNER = "       ____       _            \n"
            + "      |  _ \\ ___ | |_ ___ _ __ \n"
            + "      | |_) / _ \\| __/ _ \\ '__|\n"
            + "      |  __/  __/| ||  __/ |   \n"
            + "      |_|   \\___| \\__\\___|_|   \n";

    private final Scanner scanner;

    /** Creates a Ui that reads user input from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of user input.
     *
     * @return the trimmed line entered by the user
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Closes the input scanner. Call once, when Peter is shutting down. */
    public void close() {
        scanner.close();
    }

    /**
     * Returns Peter's greeting banner.
     *
     * @return the greeting message
     */
    public String showWelcome() {
        return BANNER + "My name is Peter\nHow am I saving you today?";
    }

    /**
     * Returns Peter's farewell message.
     *
     * @return the farewell message
     */
    public String showGoodbye() {
        return "Bye! See you next time.";
    }

    /**
     * Returns Peter's message about tasks recovered from storage.
     *
     * @param taskCount the number of tasks loaded from disk
     * @return the loading result message
     */
    public String showLoadingResult(int taskCount) {
        if (taskCount == 0) {
            return "Peter dug around but found nothing - starting a fresh list.";
        }
        return "Peter dug up " + taskCount + " task(s) from your old notes.";
    }

    /**
     * Returns Peter's message for when saved tasks could not be loaded.
     *
     * @return the loading error message
     */
    public String showLoadingError() {
        return "Peter couldn't open the old notes, so he's starting fresh.";
    }

    /**
     * Returns all tasks in the current task list.
     *
     * @param tasks the tasks to display
     * @return the task list message
     */
    public String showTaskList(List<Task> tasks) {
        StringBuilder message = new StringBuilder("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            message.append("\n").append(index + 1).append(".").append(tasks.get(index));
        }
        return message.toString();
    }

    /**
     * Returns confirmation that a task was marked as completed.
     *
     * @param task the task that was marked
     * @return the task-marked message
     */
    public String showTaskMarked(Task task) {
        return "Good job on completing:\n  " + task;
    }

    /**
     * Returns confirmation that a task was marked as incomplete.
     *
     * @param task the task that was unmarked
     * @return the task-unmarked message
     */
    public String showTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns confirmation that a task was removed.
     *
     * @param task the task that was removed
     * @param taskCount the new number of tasks
     * @return the task-removed message
     */
    public String showTaskRemoved(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Returns confirmation that a task was added.
     *
     * @param task the task that was added
     * @param taskCount the new number of tasks
     * @return the task-added message
     */
    public String showTaskAdded(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Returns tasks occurring on a specific date.
     *
     * @param matchingTasks the tasks that occur on the requested date
     * @param requestedDate the date that was requested, shown for context only
     * @return the date-query message
     */
    public String showTasksOnDate(List<Task> matchingTasks, LocalDate requestedDate) {
        if (matchingTasks.isEmpty()) {
            return "Peter checked and came up empty for that day.";
        }
        StringBuilder message = new StringBuilder("Here's what Peter dug up for that day:");
        for (Task task : matchingTasks) {
            message.append("\n  ").append(task);
        }
        return message.toString();
    }

    /**
     * Returns tasks matching a search keyword.
     *
     * @param matchingTasks the tasks that matched the search
     * @return the search-result message
     */
    public String showFoundTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return "Peter searched high and low but found nothing matching that.";
        }
        StringBuilder message = new StringBuilder("Here's what Peter dug up matching that:");
        for (int index = 0; index < matchingTasks.size(); index++) {
            message.append("\n").append(index + 1).append(".").append(matchingTasks.get(index));
        }
        return message.toString();
    }
}
