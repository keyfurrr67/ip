import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class for Peter, a command-line task manager chatbot.
 */
public class Peter {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String DATA_FILE_PATH = "./data/duke.txt";
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Starts Peter, loads saved tasks, and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = "       ____       _            \n"
                + "      |  _ \\ ___ | |_ ___ _ __ \n"
                + "      | |_) / _ \\| __/ _ \\ '__|\n"
                + "      |  __/  __/| ||  __/ |   \n"
                + "      |_|   \\___| \\__\\___|_|   \n";
        printGreeting(banner);

        Storage storage = new Storage(DATA_FILE_PATH);
        ArrayList<Task> tasks = storage.load();
        printLoadMessage(tasks.size());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                printError("yea you're gonna have to give me more than that buddy.");
                continue;
            }

            String keyword = input.split(" ", 2)[0];
            String arguments = input.length() > keyword.length()
                    ? input.substring(keyword.length()).trim() : "";
            Command command = Command.fromKeyword(keyword);
            switch (command) {
            case BYE:
                printGoodbye();
                scanner.close();
                return;
            case LIST:
                printTaskList(tasks);
                break;
            case MARK:
                markTask(arguments, tasks, storage);
                break;
            case UNMARK:
                unmarkTask(arguments, tasks, storage);
                break;
            case DELETE:
                deleteTask(arguments, tasks, storage);
                break;
            case TODO:
                addTodo(arguments, tasks, storage);
                break;
            case DEADLINE:
                addDeadline(arguments, tasks, storage);
                break;
            case EVENT:
                addEvent(arguments, tasks, storage);
                break;
            case ON:
                showTasksOnDate(arguments, tasks);
                break;
            default:
                printError("I can't recognise that cus im not that developed yet, maybe next time");
            }
        }
    }

    /**
     * Prints Peter's greeting banner.
     *
     * @param banner Peter's ASCII-art banner
     */
    private static void printGreeting(String banner) {
        System.out.println("     " + DIVIDER);
        System.out.print(banner);
        System.out.println("      My name is Peter");
        System.out.println("      How am I saving you today?");
        System.out.println("     " + DIVIDER);
    }

    /** Prints Peter's farewell message. */
    private static void printGoodbye() {
        System.out.println("     " + DIVIDER);
        System.out.println("     Bye! See you next time.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints Peter's message about tasks recovered from storage.
     *
     * @param taskCount the number of tasks loaded from disk
     */
    private static void printLoadMessage(int taskCount) {
        System.out.println("     " + DIVIDER);
        if (taskCount == 0) {
            System.out.println("     Peter dug around but found nothing - starting a fresh list.");
        } else {
            System.out.println("     Peter dug up " + taskCount + " task(s) from your old notes.");
        }
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints all tasks in the current task list.
     *
     * @param tasks the tasks to display
     */
    private static void printTaskList(ArrayList<Task> tasks) {
        System.out.println("     " + DIVIDER);
        System.out.println("     Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println("     " + (index + 1) + "." + tasks.get(index));
        }
        System.out.println("     " + DIVIDER);
    }

    /**
     * Marks a selected task as completed and saves the change.
     *
     * @param arguments the task number entered by the user
     * @param tasks the current task list
     * @param storage the task storage handler
     */
    private static void markTask(String arguments, ArrayList<Task> tasks, Storage storage) {
        Integer taskIndex = parseTaskIndex(arguments, tasks.size());
        if (taskIndex == null) {
            return;
        }
        tasks.get(taskIndex).markAsDone();
        storage.save(tasks);
        System.out.println("     " + DIVIDER);
        System.out.println("     Good job on completing:");
        System.out.println("       " + tasks.get(taskIndex));
        System.out.println("     " + DIVIDER);
    }

    /**
     * Marks a selected task as incomplete and saves the change.
     *
     * @param arguments the task number entered by the user
     * @param tasks the current task list
     * @param storage the task storage handler
     */
    private static void unmarkTask(String arguments, ArrayList<Task> tasks, Storage storage) {
        Integer taskIndex = parseTaskIndex(arguments, tasks.size());
        if (taskIndex == null) {
            return;
        }
        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks);
        System.out.println("     " + DIVIDER);
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + tasks.get(taskIndex));
        System.out.println("     " + DIVIDER);
    }

    /**
     * Deletes a selected task and saves the change.
     *
     * @param arguments the task number entered by the user
     * @param tasks the current task list
     * @param storage the task storage handler
     */
    private static void deleteTask(String arguments, ArrayList<Task> tasks, Storage storage) {
        Integer taskIndex = parseTaskIndex(arguments, tasks.size());
        if (taskIndex == null) {
            return;
        }
        Task removedTask = tasks.remove((int) taskIndex);
        storage.save(tasks);
        System.out.println("     " + DIVIDER);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + removedTask);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Adds a todo task and saves the change.
     *
     * @param arguments the todo description entered by the user
     * @param tasks the current task list
     * @param storage the task storage handler
     */
    private static void addTodo(String arguments, ArrayList<Task> tasks, Storage storage) {
        if (arguments.isEmpty()) {
            printError("yea you're gonna have to give me more than that buddy.");
            return;
        }
        tasks.add(new Todo(arguments));
        storage.save(tasks);
        printAdded(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Adds a deadline task and saves the change.
     *
     * @param arguments the deadline details entered by the user
     * @param tasks the current task list
     * @param storage the task storage handler
     */
    private static void addDeadline(String arguments, ArrayList<Task> tasks, Storage storage) {
        if (arguments.isEmpty() || !arguments.contains("/by")) {
            printError("yea you're gonna have to give me more than that buddy.");
            return;
        }
        String[] parts = arguments.split("/by", 2);
        String description = parts[0].trim();
        LocalDateTime by = parseUserDateTime(parts[1].trim());
        if (description.isEmpty() || by == null) {
            return;
        }
        tasks.add(new Deadline(description, by));
        storage.save(tasks);
        printAdded(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Adds an event task and saves the change.
     *
     * @param arguments the event details entered by the user
     * @param tasks the current task list
     * @param storage the task storage handler
     */
    private static void addEvent(String arguments, ArrayList<Task> tasks, Storage storage) {
        if (arguments.isEmpty() || !arguments.contains("/from") || !arguments.contains("/to")) {
            printError("yea you're gonna have to give me more than that buddy.");
            return;
        }
        String[] fromParts = arguments.split("/from", 2);
        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split("/to", 2);
        String fromText = toParts[0].trim();
        String toText = toParts.length > 1 ? toParts[1].trim() : "";
        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            printError("yea you're gonna have to give me more than that buddy.");
            return;
        }
        LocalDateTime from = parseUserDateTime(fromText);
        LocalDateTime to = parseUserDateTime(toText);
        if (from == null || to == null) {
            return;
        }
        tasks.add(new Event(description, from, to));
        storage.save(tasks);
        printAdded(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Shows deadlines due on a date and events that start on that date.
     *
     * @param arguments the requested date
     * @param tasks the current task list
     */
    private static void showTasksOnDate(String arguments, ArrayList<Task> tasks) {
        LocalDate requestedDate = parseUserDate(arguments);
        if (requestedDate == null) {
            return;
        }
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline
                    && ((Deadline) task).getBy().toLocalDate().equals(requestedDate)) {
                matchingTasks.add(task);
            } else if (task instanceof Event
                    && ((Event) task).getFrom().toLocalDate().equals(requestedDate)) {
                matchingTasks.add(task);
            }
        }
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
     * Parses a user date and time, defaulting date-only values to midnight.
     *
     * @param dateTimeText the user-entered date and optional time
     * @return the parsed date and time, or {@code null} if the input is invalid
     */
    private static LocalDateTime parseUserDateTime(String dateTimeText) {
        try {
            if (dateTimeText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(dateTimeText, INPUT_DATE_FORMAT).atStartOfDay();
            }
            return LocalDateTime.parse(dateTimeText, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            printError("that date's not making sense to me buddy, try yyyy-MM-dd or "
                    + "yyyy-MM-dd HHmm.");
            return null;
        }
    }

    /**
     * Parses a date used by the {@code on} command.
     *
     * @param dateText the user-entered date
     * @return the parsed date, or {@code null} if the input is invalid
     */
    private static LocalDate parseUserDate(String dateText) {
        try {
            return LocalDate.parse(dateText, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            printError("that date's not making sense to me buddy, try yyyy-MM-dd.");
            return null;
        }
    }

    /**
     * Converts a user-entered task number into a valid zero-based list index.
     *
     * @param arguments the task number entered by the user
     * @param taskCount the current number of tasks
     * @return the zero-based task index, or {@code null} for invalid input
     */
    private static Integer parseTaskIndex(String arguments, int taskCount) {
        if (arguments.isEmpty()) {
            printError("yea you're gonna have to give me more than that buddy.");
            return null;
        }
        try {
            int taskIndex = Integer.parseInt(arguments) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                printError("that task doesn't exist buddy.");
                return null;
            }
            return taskIndex;
        } catch (NumberFormatException exception) {
            printError("that's not a number my guy.");
            return null;
        }
    }

    /**
     * Prints confirmation that Peter added a task.
     *
     * @param task the task that was added
     * @param taskCount the new number of tasks
     */
    private static void printAdded(Task task, int taskCount) {
        System.out.println("     " + DIVIDER);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("     " + DIVIDER);
    }

    /**
     * Prints a boxed error message in Peter's usual style.
     *
     * @param message the error message to display
     */
    private static void printError(String message) {
        System.out.println("     " + DIVIDER);
        System.out.println("     " + message);
        System.out.println("     " + DIVIDER);
    }
}
