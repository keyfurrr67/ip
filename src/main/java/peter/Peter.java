package peter;

import java.time.LocalDate;

/**
 * The main class for Peter, a command-line task manager chatbot.
 */
public class Peter {
    private static final String DATA_FILE_PATH = "./data/duke.txt";

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates Peter, loading any previously saved tasks from the given file.
     *
     * @param filePath the location of the task data file
     */
    public Peter(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            ui.showLoadingResult(tasks.size());
        } catch (PeterException exception) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /** Runs Peter's main command loop until the user exits. */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;
        while (isRunning) {
            try {
                String input = ui.readCommand();
                Parser.ParsedInput parsedInput = Parser.parseInput(input);
                isRunning = handleCommand(parsedInput);
            } catch (PeterException exception) {
                ui.showError(exception.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Handles one parsed command, mutating the task list and reporting via the UI.
     *
     * @param parsedInput the command and arguments to act on
     * @return {@code false} if this command should end the program, otherwise {@code true}
     * @throws PeterException if the command's arguments are invalid
     */
    private boolean handleCommand(Parser.ParsedInput parsedInput) throws PeterException {
        String arguments = parsedInput.getArguments();
        switch (parsedInput.getCommand()) {
            case BYE:
                ui.showGoodbye();
                return false;
            case LIST:
                ui.showTaskList(tasks.getAll());
                break;
            case MARK: {
                int index = Parser.parseTaskIndex(arguments);
                Task task = tasks.mark(index);
                storage.save(tasks.getAll());
                ui.showTaskMarked(task);
                break;
            }
            case UNMARK: {
                int index = Parser.parseTaskIndex(arguments);
                Task task = tasks.unmark(index);
                storage.save(tasks.getAll());
                ui.showTaskUnmarked(task);
                break;
            }
            case DELETE: {
                int index = Parser.parseTaskIndex(arguments);
                Task removedTask = tasks.delete(index);
                storage.save(tasks.getAll());
                ui.showTaskRemoved(removedTask, tasks.size());
                break;
            }
            case TODO: {
                Task task = Parser.parseTodo(arguments);
                tasks.add(task);
                storage.save(tasks.getAll());
                ui.showTaskAdded(task, tasks.size());
                break;
            }
            case DEADLINE: {
                Task task = Parser.parseDeadline(arguments);
                tasks.add(task);
                storage.save(tasks.getAll());
                ui.showTaskAdded(task, tasks.size());
                break;
            }
            case EVENT: {
                Task task = Parser.parseEvent(arguments);
                tasks.add(task);
                storage.save(tasks.getAll());
                ui.showTaskAdded(task, tasks.size());
                break;
            }
            case ON: {
                LocalDate requestedDate = Parser.parseDate(arguments);
                ui.showTasksOnDate(tasks.getTasksOnDate(requestedDate), requestedDate);
                break;
            }
            default:
                throw new PeterException("I can't recognise that cus im not that developed yet, maybe next time");
        }
        return true;
    }

    /**
     * Starts Peter.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Peter(DATA_FILE_PATH).run();
    }
}