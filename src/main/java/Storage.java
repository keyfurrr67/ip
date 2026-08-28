import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving tasks to and loading tasks from a text file.
 */
public class Storage {
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";

    private final Path filePath;

    /**
     * Creates storage that uses the given relative or absolute file path.
     *
     * @param filePathString the location of the task data file
     */
    public Storage(String filePathString) {
        this.filePath = Path.of(filePathString);
    }

    /**
     * Loads all valid tasks from the data file. Malformed lines are skipped so
     * that one bad entry does not prevent the remaining tasks from loading.
     *
     * @return the tasks successfully loaded from disk
     */
    public ArrayList<Task> load() {
        ArrayList<Task> loadedTasks = new ArrayList<>();

        try {
            ensureDataFileExists();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            for (String line : lines) {
                Task task = parseTask(line);
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
        } catch (IOException exception) {
            System.out.println("     Peter couldn't open the old notes, so he's starting fresh.");
        }

        return loadedTasks;
    }

    /**
     * Saves the given tasks to the data file, replacing its previous contents.
     *
     * @param tasks the current list of tasks to save
     */
    public void save(ArrayList<Task> tasks) {
        ArrayList<String> taskLines = new ArrayList<>();

        for (Task task : tasks) {
            taskLines.add(task.toFileFormat());
        }

        try {
            ensureDataFileExists();
            Files.write(filePath, taskLines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            System.out.println("     Peter tried to save your notes, but the drawer got stuck.");
        }
    }

    /**
     * Creates the data directory and data file when they do not already exist.
     *
     * @throws IOException if the directory or file cannot be created
     */
    private void ensureDataFileExists() throws IOException {
        Path parentDirectory = filePath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Converts one storage-format line into a task.
     *
     * @param line one line from the data file
     * @return the parsed task, or {@code null} when the line is malformed
     */
    private Task parseTask(String line) {
        String[] parts = line.split("\\|", -1);

        for (int index = 0; index < parts.length; index++) {
            parts[index] = parts[index].trim();
        }

        if (parts.length < 3 || !isValidStatus(parts[1]) || parts[2].isEmpty()) {
            printSkippedLineWarning(line);
            return null;
        }

        Task task;

        switch (parts[0]) {
        case TODO_TYPE:
            if (parts.length != 3) {
                printSkippedLineWarning(line);
                return null;
            }
            task = new Todo(parts[2]);
            break;
        case DEADLINE_TYPE:
            if (parts.length != 4 || parts[3].isEmpty()) {
                printSkippedLineWarning(line);
                return null;
            }
            task = new Deadline(parts[2], parts[3]);
            break;
        case EVENT_TYPE:
            if (parts.length != 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                printSkippedLineWarning(line);
                return null;
            }
            task = new Event(parts[2], parts[3], parts[4]);
            break;
        default:
            printSkippedLineWarning(line);
            return null;
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Checks whether a saved completion state is valid.
     *
     * @param status the status value from the storage file
     * @return true if the status is either 0 or 1
     */
    private boolean isValidStatus(String status) {
        return status.equals("0") || status.equals("1");
    }

    /**
     * Prints Peter's warning for an unreadable saved task line.
     *
     * @param line the malformed line that was skipped
     */
    private void printSkippedLineWarning(String line) {
        System.out.println("     Peter found a scribbled-on line he couldn't read: \""
                + line + "\" — skipping it.");
    }
}
