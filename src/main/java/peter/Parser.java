package peter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Makes sense of raw user input: splitting it into a command and arguments,
 * and parsing arguments into task data. Contains no console I/O and does
 * not mutate the task list directly.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Represents a raw line of input split into its command keyword and the
     * remaining arguments.
     */
    public static class ParsedInput {
        private final Command command;
        private final String arguments;

        /**
         * Creates a parsed input.
         *
         * @param command the recognised command
         * @param arguments the remaining text after the command keyword
         */
        public ParsedInput(Command command, String arguments) {
            this.command = command;
            this.arguments = arguments;
        }

        /**
         * Returns the recognised command.
         *
         * @return the command
         */
        public Command getCommand() {
            return command;
        }

        /**
         * Returns the arguments that followed the command keyword.
         *
         * @return the trimmed argument text
         */
        public String getArguments() {
            return arguments;
        }
    }

    /**
     * Splits a raw line of user input into a command and its arguments.
     *
     * @param rawInput the full line entered by the user
     * @return the parsed input
     * @throws PeterException if the input is empty
     */
    public static ParsedInput parseInput(String rawInput) throws PeterException {
        if (rawInput.isEmpty()) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        String keyword = rawInput.split(" ", 2)[0];
        String arguments = rawInput.length() > keyword.length()
                ? rawInput.substring(keyword.length()).trim() : "";
        return new ParsedInput(Command.fromKeyword(keyword), arguments);
    }

    /**
     * Parses arguments for a todo command.
     *
     * @param arguments the todo description entered by the user
     * @return the new todo task
     * @throws PeterException if the description is empty
     */
    public static Todo parseTodo(String arguments) throws PeterException {
        if (arguments.isEmpty()) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        return new Todo(arguments);
    }

    /**
     * Parses arguments for a deadline command in the form
     * {@code <description> /by <date/time>}.
     *
     * @param arguments the deadline details entered by the user
     * @return the new deadline task
     * @throws PeterException if the details are missing or the date is invalid
     */
    public static Deadline parseDeadline(String arguments) throws PeterException {
        if (arguments.isEmpty() || !arguments.contains("/by")) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        String[] parts = arguments.split("/by", 2);
        String description = parts[0].trim();
        String byText = parts[1].trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        LocalDateTime by = parseUserDateTime(byText);
        return new Deadline(description, by);
    }

    /**
     * Parses arguments for an event command in the form
     * {@code <description> /from <date/time> /to <date/time>}.
     *
     * @param arguments the event details entered by the user
     * @return the new event task
     * @throws PeterException if the details are missing or a date is invalid
     */
    public static Event parseEvent(String arguments) throws PeterException {
        if (arguments.isEmpty() || !arguments.contains("/from") || !arguments.contains("/to")) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        String[] fromParts = arguments.split("/from", 2);
        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split("/to", 2);
        String fromText = toParts[0].trim();
        String toText = toParts.length > 1 ? toParts[1].trim() : "";
        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        LocalDateTime from = parseUserDateTime(fromText);
        LocalDateTime to = parseUserDateTime(toText);
        return new Event(description, from, to);
    }

    /**
     * Parses the task number for mark/unmark/delete commands into a
     * zero-based index. Does not check the index against the task list size.
     *
     * @param arguments the task number entered by the user
     * @return the zero-based task index
     * @throws PeterException if the argument is missing or not a number
     */
    public static int parseTaskIndex(String arguments) throws PeterException {
        if (arguments.isEmpty()) {
            throw new PeterException("yea you're gonna have to give me more than that buddy.");
        }
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException exception) {
            throw new PeterException("that's not a number my guy.");
        }
    }

    /**
     * Parses the date used by the {@code on} command.
     *
     * @param arguments the date entered by the user
     * @return the parsed date
     * @throws PeterException if the date is invalid
     */
    public static LocalDate parseDate(String arguments) throws PeterException {
        try {
            return LocalDate.parse(arguments, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new PeterException("that date's not making sense to me buddy, try yyyy-MM-dd.");
        }
    }

    /**
     * Parses a user date and time, defaulting date-only values to midnight.
     *
     * @param dateTimeText the user-entered date and optional time
     * @return the parsed date and time
     * @throws PeterException if the text does not match a recognised format
     */
    private static LocalDateTime parseUserDateTime(String dateTimeText) throws PeterException {
        try {
            if (dateTimeText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(dateTimeText, INPUT_DATE_FORMAT).atStartOfDay();
            }
            return LocalDateTime.parse(dateTimeText, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new PeterException("that date's not making sense to me buddy, try yyyy-MM-dd or "
                    + "yyyy-MM-dd HHmm.");
        }
    }
}