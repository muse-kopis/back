package muse_kopis.muse.performance.dto;

import lombok.Builder;

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
}
