package muse_kopis.muse.ticketbook.dto;

import java.time.LocalDateTime;
import java.util.List;
import muse_kopis.muse.ticketbook.photo.dto.PhotoResponse;

public record TicketBookEditRequest(
        Long performanceId,
        LocalDateTime viewDate,
        String castMembers,
        String content,
        Integer star,
        Boolean visible
) {
}
