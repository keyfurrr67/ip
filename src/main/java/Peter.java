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
        //echo loop
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

            tasks[taskCount] = new Task(input);
            taskCount++;
            System.out.println("     " + line);
            System.out.println("     added: " + input);
            System.out.println("     " + line);
        }

        scanner.close();

    }
}
