package muse_kopis.muse.common.ticketbook;

public class NotFoundTicketBookException extends RuntimeException {

    public NotFoundTicketBookException(String message) {
        super(message);
    }
}
