/**
 * Lists the commands understood by Peter.
 */
public enum Command {
    BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN;

    /**
     * Converts a command keyword into its matching command value.
     *
     * @param keyword the command keyword entered by the user
     * @return the matching command, or {@link #UNKNOWN} if there is no match
     */
    public static Command fromKeyword(String keyword) {
        try {
            return Command.valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
