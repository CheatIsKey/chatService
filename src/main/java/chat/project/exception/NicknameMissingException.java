package chat.project.exception;

public class NicknameMissingException extends RuntimeException {
    public NicknameMissingException() {
        super("Nickname is required. Get suggestions first, then choose one.");
    }
}
