package peter;

/**
 * Represents a todo task with no date or time information.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description the todo description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo in the format used by Peter's storage file.
     *
     * @return the todo's pipe-delimited storage representation
     */
    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns this todo in Peter's normal display format.
     *
     * @return the formatted todo
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
