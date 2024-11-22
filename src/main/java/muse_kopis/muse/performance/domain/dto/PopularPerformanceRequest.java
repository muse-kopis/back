package muse_kopis.muse.performance.domain.dto;

public record PopularPerformanceRequest(
        String type,
        String date,
        String genre
) {
}
