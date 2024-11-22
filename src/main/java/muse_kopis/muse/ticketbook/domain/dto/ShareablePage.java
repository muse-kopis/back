package muse_kopis.muse.ticketbook.domain.dto;

import java.util.List;

public record ShareablePage(
        List<TicketBookResponse> ticketBooks,
        String username
) {
}
