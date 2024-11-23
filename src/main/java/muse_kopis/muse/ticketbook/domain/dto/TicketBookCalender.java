package muse_kopis.muse.ticketbook.domain.dto;

import muse_kopis.muse.ticketbook.domain.TicketBook;

public record TicketBookCalender(
        Long ticketBookId,
        String poster
) {
    public static TicketBookCalender from(TicketBook ticketBook) {
        return new TicketBookCalender(ticketBook.getId(), ticketBook.getReview().getPerformance().getPoster());
    }
}
