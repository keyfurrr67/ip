import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading input and printing output.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
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

    /** Prints Peter's greeting banner. */
    public void showWelcome() {
        System.out.println("     " + DIVIDER);
        System.out.print(BANNER);
        System.out.println("      My name is Peter");
        System.out.println("      How am I saving you today?");
        System.out.println("     " + DIVIDER);
    }

    /** Prints Peter's farewell message. */
    public void showGoodbye() {
        System.out.println("     " + DIVIDER);
        System.out.println("     Bye! See you next time.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints Peter's message about tasks recovered from storage.
     *
     * @param taskCount the number of tasks loaded from disk
     */
    public void showLoadingResult(int taskCount) {
        System.out.println("     " + DIVIDER);
        if (taskCount == 0) {
            System.out.println("     Peter dug around but found nothing - starting a fresh list.");
        } else {
            System.out.println("     Peter dug up " + taskCount + " task(s) from your old notes.");
        }
        System.out.println("     " + DIVIDER);
    }

    /** Prints Peter's message for when saved tasks could not be loaded. */
    public void showLoadingError() {
        System.out.println("     " + DIVIDER);
        System.out.println("     Peter couldn't open the old notes, so he's starting fresh.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints all tasks in the current task list.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("     " + DIVIDER);
        System.out.println("     Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println("     " + (index + 1) + "." + tasks.get(index));
        }
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints confirmation that a task was marked as completed.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("     " + DIVIDER);
        System.out.println("     Good job on completing:");
        System.out.println("       " + task);
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints confirmation that a task was marked as incomplete.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("     " + DIVIDER);
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + task);
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints confirmation that a task was removed.
     *
     * @param task the task that was removed
     * @param taskCount the new number of tasks
     */
    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println("     " + DIVIDER);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints confirmation that a task was added.
     *
     * @param task the task that was added
     * @param taskCount the new number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("     " + DIVIDER);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints tasks occurring on a specific date.
     *
     * @param matchingTasks the tasks that occur on the requested date
     * @param requestedDate the date that was requested, shown for context only
     */
    public void showTasksOnDate(List<Task> matchingTasks, LocalDate requestedDate) {
        System.out.println("     " + DIVIDER);
        if (matchingTasks.isEmpty()) {
            System.out.println("     Peter checked and came up empty for that day.");
        } else {
            System.out.println("     Here's what Peter dug up for that day:");
            for (Task task : matchingTasks) {
                System.out.println("       " + task);
            }
        }
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints a boxed error message in Peter's usual style.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println("     " + DIVIDER);
        System.out.println("     " + message);
        System.out.println("     " + DIVIDER);
    }
}