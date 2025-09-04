package chat.project.exception;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(String username) {
        super("Nickname already exists: " + username);
    }
}
