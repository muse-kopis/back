package muse_kopis.muse.ticketbook.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TicketBookEditRequest(
        Long performanceId,
        LocalDateTime viewDate,
        String castMembers,
        String content,
        Integer star,
        Boolean visible,
        List<String> photos
) {
}
