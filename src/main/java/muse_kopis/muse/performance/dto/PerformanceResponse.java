package muse_kopis.muse.performance.dto;

import java.time.LocalDate;
import lombok.Builder;
import muse_kopis.muse.performance.Performance;

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
    public static PerformanceResponse from(Performance performance) {
        return PerformanceResponse.builder()
                .performanceName(performance.getPerformanceName())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .venue(performance.getVenue())
                .poster(performance.getPoster())
                .performanceTime(performance.getPerformanceTime())
                .limitAge(performance.getLimitAge())
                .performanceCrews(performance.getPerformanceCrews())
                .entertainment(performance.getEntertainment())
                .build();
    }
}
