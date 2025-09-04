package chat.project.exception;

public class NicknameAlreadyReservedException extends RuntimeException {
    public NicknameAlreadyReservedException(String nickname) {
        super("Nickname already reserved: " + nickname);
    }
}
