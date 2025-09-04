package chat.project.exception;

public class ReservationInvalidOrExpiredException extends RuntimeException {
    public ReservationInvalidOrExpiredException(String nickname) {
        super("Reservation is invalid or expired for nickname: " + nickname);
    }
}
