import java.util.Scanner;

public class Peter {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        String banner = "       ____       _            \n"
                + "      |  _ \\ ___ | |_ ___ _ __ \n"
                + "      | |_) / _ \\| __/ _ \\ '__|\n"
                + "      |  __/  __/| ||  __/ |   \n"
                + "      |_|   \\___| \\__\\___|_|   \n";

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

            if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                tasks[index].markAsDone();
                System.out.println("     " + line);
                System.out.println("     Good job on completing:");
                System.out.println("       " + tasks[index]);
                System.out.println("     " + line);
                continue;
            }

            if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                tasks[index].markAsNotDone();
                System.out.println("     " + line);
                System.out.println("     OK, I've marked this task as not done yet:");
                System.out.println("       " + tasks[index]);
                System.out.println("     " + line);
                continue;
            }

            if (input.startsWith("todo ")) {
                String description = input.substring(5).trim();
                tasks[taskCount] = new Todo(description);
                taskCount++;
                printAdded(line, tasks[taskCount - 1], taskCount);
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                String[] parts = rest.split("/by", 2);
                String description = parts[0].trim();
                String by = parts.length > 1 ? parts[1].trim() : "";
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                printAdded(line, tasks[taskCount - 1], taskCount);
                continue;
            }

            if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                String[] fromSplit = rest.split("/from", 2);
                String description = fromSplit[0].trim();
                String from = "";
                String to = "";
                if (fromSplit.length > 1) {
                    String[] toSplit = fromSplit[1].split("/to", 2);
                    from = toSplit[0].trim();
                    to = toSplit.length > 1 ? toSplit[1].trim() : "";
                }
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                printAdded(line, tasks[taskCount - 1], taskCount);
                continue;
            }

            System.out.println("     " + line);
            System.out.println("     OOPS!!! I don't recognise that command :-(");
            System.out.println("     " + line);
        }

        scanner.close();
    }

    private static void printAdded(String line, Task task, int taskCount) {
        System.out.println("     " + line);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("     " + line);
    }
}