package muse_kopis.muse.ticketbook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import muse_kopis.muse.review.dto.ReviewResponse;
import org.springframework.web.multipart.MultipartFile;

@Schema
public record TicketBookRequest(
        Long performanceId,
        LocalDate viewDate,
        ReviewResponse review,
        List<MultipartFile> photos
) {
}
