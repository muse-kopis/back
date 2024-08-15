package muse_kopis.muse.performance.dto;

import lombok.Builder;
import muse_kopis.muse.performance.dto.Boxofs.Boxof;

@Builder
public record PopularPerformanceResponse(
        String venue,
        Integer rank,
        String poster,
        String performancePeriod,
        String performanceName,
        Integer performanceCount,
        String area
) {
    public static PopularPerformanceResponse from(Boxof performance) {
        return PopularPerformanceResponse.builder()
                .venue(performance.prfplcnm())
                .rank(performance.rnum())
                .poster(performance.poster())
                .performancePeriod(performance.prfpd())
                .performanceName(performance.prfnm())
                .performanceCount(performance.prfdtcnt())
                .area(performance.area())
                .build();
    }
}
