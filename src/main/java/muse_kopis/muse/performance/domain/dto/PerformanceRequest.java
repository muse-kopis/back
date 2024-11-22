package muse_kopis.muse.performance.domain.dto;

public record PerformanceRequest(
        String startDate,
        String endDate,
        String currentPage,
        String rows,
        String state,
        String genre // 장르코드
) {
}
