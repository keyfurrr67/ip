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

        String[] tasks = new String[100];
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

            if(input.equals("list")) {
                System.out.println("     " + line);
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("     " + line);
                continue;
            }

            tasks[taskCount] = input;
            taskCount++;
            System.out.println("     " + line);
            System.out.println("     added: " + input);
            System.out.println("     " + line);
        }

        scanner.close();

    }
}
