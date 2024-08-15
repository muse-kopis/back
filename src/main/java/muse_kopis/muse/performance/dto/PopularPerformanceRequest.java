package muse_kopis.muse.performance.dto;

public record PopularPerformanceRequest(
        String type,
        String date,
        String genre
) {
}
