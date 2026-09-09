package peter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that occurs during a specified period.
 */
public class Event extends Task {
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);
    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmm");

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an incomplete event task.
     *
     * @param description the event description
     * @param from the event start date and time
     * @param to the event end date and time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        assert !to.isBefore(from) : "event end should not be before its start";
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start date and time.
     *
     * @return the event start date and time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns this event in the format used by Peter's storage file.
     *
     * @return the event's pipe-delimited storage representation
     */
    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? DONE_MARKER : NOT_DONE_MARKER) + " | " + description
                + " | " + from.format(STORAGE_DATE_FORMAT) + " | " + to.format(STORAGE_DATE_FORMAT);
    }

    /**
     * Returns this event in Peter's normal display format.
     *
     * @return the formatted event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + formatDateTime(from)
                + " to: " + formatDateTime(to) + ")";
    }

    /**
     * Formats a date and time for display without showing midnight unnecessarily.
     *
     * @param dateTime the date and time to format
     * @return the formatted date, with time when it is not midnight
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dateTime.format(OUTPUT_DATE_FORMAT);
        }
        return dateTime.format(OUTPUT_DATE_TIME_FORMAT);
    }
}
