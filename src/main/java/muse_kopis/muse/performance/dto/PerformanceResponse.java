package muse_kopis.muse.performance.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record PerformanceResponse(
        String performanceName,
        LocalDate startDate,
        LocalDate endDate,
        String poster,
        String venue,
        String performanceTime,
        String limitAge,
        String performanceCrews,
        String entertainment
//        String ticketing,
//        String ticketingURL
) {
}
