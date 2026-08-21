import java.util.Scanner;

public class Peter {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
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

        Task[] tasks = new Task[100];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("     " + line);
                System.out.println("     Bye! See you next time.");
                System.out.println("     " + line);
                break;
            }

            if (input.equals("list")) {
                System.out.println("     " + line);
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
                System.out.println("     " + line);
                continue;
            }

            if (input.equals("mark") || input.startsWith("mark ")) {
                String arg = input.length() > 4 ? input.substring(4).trim() : "";
                Integer index = parseTaskIndex(arg, taskCount, line);
                if (index == null) {
                    continue;
                }
                tasks[index].markAsDone();
                System.out.println("     " + line);
                System.out.println("     Good job on completing:");
                System.out.println("       " + tasks[index]);
                System.out.println("     " + line);
                continue;
            }

            if (input.equals("unmark") || input.startsWith("unmark ")) {
                String arg = input.length() > 6 ? input.substring(6).trim() : "";
                Integer index = parseTaskIndex(arg, taskCount, line);
                if (index == null) {
                    continue;
                }
                tasks[index].markAsNotDone();
                System.out.println("     " + line);
                System.out.println("     OK, I've marked this task as not done yet:");
                System.out.println("       " + tasks[index]);
                System.out.println("     " + line);
                continue;
            }

            if (input.equals("todo") || input.startsWith("todo ")) {
                String description = input.length() > 4 ? input.substring(4).trim() : "";
                if (description.isEmpty()) {
                    printError(line, "yea you're gonna have to give me more than that buddy.");
                    continue;
                }
                tasks[taskCount] = new Todo(description);
                taskCount++;
                printAdded(line, tasks[taskCount - 1], taskCount);
                continue;
            }

            if (input.equals("deadline") || input.startsWith("deadline ")) {
                String rest = input.length() > 8 ? input.substring(8).trim() : "";
                if (rest.isEmpty() || !rest.contains("/by")) {
                    printError(line, "yea you're gonna have to give me more than that buddy.");
                    continue;
                }
                String[] parts = rest.split("/by", 2);
                String description = parts[0].trim();
                String by = parts[1].trim();
                if (description.isEmpty() || by.isEmpty()) {
                    printError(line, "yea you're gonna have to give me more than that buddy.");
                    continue;
                }
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                printAdded(line, tasks[taskCount - 1], taskCount);
                continue;
            }

            if (input.equals("event") || input.startsWith("event ")) {
                String rest = input.length() > 5 ? input.substring(5).trim() : "";
                if (rest.isEmpty() || !rest.contains("/from") || !rest.contains("/to")) {
                    printError(line, "yea you're gonna have to give me more than that buddy.");
                    continue;
                }
                String[] fromSplit = rest.split("/from", 2);
                String description = fromSplit[0].trim();
                String[] toSplit = fromSplit[1].split("/to", 2);
                String from = toSplit[0].trim();
                String to = toSplit.length > 1 ? toSplit[1].trim() : "";
                if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    printError(line, "yea you're gonna have to give me more than that buddy.");
                    continue;
                }
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                printAdded(line, tasks[taskCount - 1], taskCount);
                continue;
            }

            printError(line, "I can't recognise that cus im not that developed yet, maybe next time");
        }

        scanner.close();
    }

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

    private static void printAdded(String line, Task task, int taskCount) {
        System.out.println("     " + line);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("     " + line);
    }

    private static void printError(String line, String message) {
        System.out.println("     " + line);
        System.out.println("     " + message);
        System.out.println("     " + line);
    }
}