/**
 * Represents a task that occurs during a specified time period.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description the event description
     * @param from the event start time or date
     * @param to the event end time or date
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the format used by Peter's storage file.
     *
     * @return the event's pipe-delimited storage representation
     */
    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + description
                + " | " + from + " | " + to;
    }

    /**
     * Returns this event in Peter's normal display format.
     *
     * @return the formatted event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
