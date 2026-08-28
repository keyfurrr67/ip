import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class for Peter, a command-line task manager chatbot.
 */
public class Peter {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String DATA_FILE_PATH = "./data/duke.txt";

    /**
     * Starts Peter, loads saved tasks, and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String line = DIVIDER;
        String banner = "       ____       _            \n"
                      + "      |  _ \\ ___ | |_ ___ _ __ \n"
                      + "      | |_) / _ \\| __/ _ \\ '__|\n"
                      + "      |  __/  __/| ||  __/ |   \n"
                      + "      |_|   \\___| \\__\\___|_|   \n";

        //greetings
        System.out.println("     " + line);
        System.out.print(banner);
        System.out.println("      My name is Peter");
        System.out.println("      How am I saving you today?");
        System.out.println("     " + line);

        Storage storage = new Storage(DATA_FILE_PATH);
        ArrayList<Task> tasks = storage.load();
        printLoadMessage(tasks.size(), line);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();

            String keyword = input.split(" ", 2)[0];
            String argsRest = input.length() > keyword.length() ? input.substring(keyword.length()).trim() : "";
            Command command = Command.fromKeyword(keyword);

            //different commands given by user
            switch (command) {
                case BYE:
                    System.out.println("     " + line);
                    System.out.println("     Bye! See you next time.");
                    System.out.println("     " + line);
                    scanner.close();
                    return;

                //list out full list
                case LIST:
                    System.out.println("     " + line);
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println("     " + line);
                    break;

                //user chooses activity to be marked as done
                case MARK: {
                    Integer index = parseTaskIndex(argsRest, tasks.size(), line);
                    if (index == null) {
                        break;
                    }
                    tasks.get(index).markAsDone();
                    storage.save(tasks);
                    System.out.println("     " + line);
                    System.out.println("     Good job on completing:");
                    System.out.println("       " + tasks.get(index));
                    System.out.println("     " + line);
                    break;
                }

                //user chooses to undo an activity
                case UNMARK: {
                    Integer index = parseTaskIndex(argsRest, tasks.size(), line);
                    if (index == null) {
                        break;
                    }
                    tasks.get(index).markAsNotDone();
                    storage.save(tasks);
                    System.out.println("     " + line);
                    System.out.println("     OK, I've marked this task as not done yet:");
                    System.out.println("       " + tasks.get(index));
                    System.out.println("     " + line);
                    break;
                }

                //completely remove an item from the list
                case DELETE: {
                    Integer index = parseTaskIndex(argsRest, tasks.size(), line);
                    if (index == null) {
                        break;
                    }
                    Task removed = tasks.remove((int) index);
                    storage.save(tasks);
                    System.out.println("     " + line);
                    System.out.println("     Noted. I've removed this task:");
                    System.out.println("       " + removed);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("     " + line);
                    break;
                }

                //add an activity to do
                case TODO:
                    if (argsRest.isEmpty()) {
                        printError(line, "yea you're gonna have to give me more than that buddy.");
                        break;
                    }
                    tasks.add(new Todo(argsRest));
                    storage.save(tasks);
                    printAdded(line, tasks.get(tasks.size() - 1), tasks.size());
                    break;

                //add an activity with a deadline in the format deadline xxx /by xxx
                case DEADLINE: {
                    if (argsRest.isEmpty() || !argsRest.contains("/by")) {
                        printError(line, "yea you're gonna have to give me more than that buddy.");
                        break;
                    }
                    String[] parts = argsRest.split("/by", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    if (description.isEmpty() || by.isEmpty()) {
                        printError(line, "yea you're gonna have to give me more than that buddy.");
                        break;
                    }
                    tasks.add(new Deadline(description, by));
                    storage.save(tasks);
                    printAdded(line, tasks.get(tasks.size() - 1), tasks.size());
                    break;
                }

                //add an activity in the format event xxx /from xxx /to xxx
                case EVENT: {
                    if (argsRest.isEmpty() || !argsRest.contains("/from") || !argsRest.contains("/to")) {
                        printError(line, "yea you're gonna have to give me more than that buddy.");
                        break;
                    }
                    String[] fromSplit = argsRest.split("/from", 2);
                    String description = fromSplit[0].trim();
                    String[] toSplit = fromSplit[1].split("/to", 2);
                    String from = toSplit[0].trim();
                    String to = toSplit.length > 1 ? toSplit[1].trim() : "";
                    if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        printError(line, "yea you're gonna have to give me more than that buddy.");
                        break;
                    }
                    tasks.add(new Event(description, from, to));
                    storage.save(tasks);
                    printAdded(line, tasks.get(tasks.size() - 1), tasks.size());
                    break;
                }
                //unknown commands not specified in switch
                default:
                    printError(line, "I can't recognise that cus im not that developed yet, maybe next time");
            }
        }
    }
    /**
     * Prints Peter's message about tasks recovered from storage.
     *
     * @param taskCount the number of tasks loaded from disk
     * @param line the divider used in Peter's boxed console output
     */
    private static void printLoadMessage(int taskCount, String line) {
        System.out.println("     " + line);
        if (taskCount == 0) {
            System.out.println("     Peter dug around but found nothing — starting a fresh list.");
        } else {
            System.out.println("     Peter dug up " + taskCount + " task(s) from your old notes.");
        }
        System.out.println("     " + line);
    }

    /**
     * Converts a user-entered task number into a valid zero-based list index.
     *
     * @param arg the task number entered by the user
     * @param taskCount the current number of tasks
     * @param line the divider used in Peter's boxed console output
     * @return the zero-based task index, or {@code null} for invalid input
     */
    private static Integer parseTaskIndex(String arg, int taskCount, String line) {
        if (arg.isEmpty()) {
            printError(line, "yea you're gonna have to give me more than that buddy.");
            return null;
        }
        int index;
        try {
            index = Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            printError(line, "that's not a number my guy.");
            return null;
        }
        if (index < 0 || index >= taskCount) {
            printError(line, "that task doesn't exist buddy.");
            return null;
        }
        return index;
    }

    /**
     * Prints confirmation that Peter added a task.
     *
     * @param line the divider used in Peter's boxed console output
     * @param task the task that was added
     * @param taskCount the new number of tasks
     */
    private static void printAdded(String line, Task task, int taskCount) {
        System.out.println("     " + line);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("     " + line);
    }

    /**
     * Prints a boxed error message in Peter's usual style.
     *
     * @param line the divider used in Peter's boxed console output
     * @param message the error message to display
     */
    private static void printError(String line, String message) {
        System.out.println("     " + line);
        System.out.println("     " + message);
        System.out.println("     " + line);
    }
}
