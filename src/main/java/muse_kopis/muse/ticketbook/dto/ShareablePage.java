package muse_kopis.muse.ticketbook.dto;

import java.util.List;

public record ShareablePage(
        List<TicketBookResponse> ticketBooks,
        String username
) {
}
