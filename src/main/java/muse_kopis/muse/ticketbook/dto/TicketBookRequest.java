package muse_kopis.muse.ticketbook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Schema
public record TicketBookRequest(
        Long performanceId,
        LocalDateTime viewDate,
        String castMembers,
        String content,
        Integer star,
        Boolean visible,
        List<MultipartFile> photos
) {
}
