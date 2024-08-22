package muse_kopis.muse.ticketbook.dto;

import java.time.LocalDate;
import java.util.List;
import muse_kopis.muse.review.dto.ReviewResponse;

public record TicketBookRequest(
        Long performanceId,
        LocalDate viewDate,
        ReviewResponse review,
        List<PhotoDto> photos
) {
}
