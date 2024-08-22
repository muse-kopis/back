package muse_kopis.muse.performance.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.castmember.dto.CastMemberDto;

@Builder
public record PerformanceResponse(
        Long id,
        String performanceName,
        LocalDate startDate,
        LocalDate endDate,
        String poster,
        String venue,
        String performanceTime,
        String limitAge,
        List<CastMemberDto> castMembers,
        String entertainment
) {
    public static PerformanceResponse from(Performance performance) {
        return PerformanceResponse.builder()
                .id(performance.getId())
                .performanceName(performance.getPerformanceName())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .venue(performance.getVenue())
                .poster(performance.getPoster())
                .performanceTime(performance.getPerformanceTime())
                .limitAge(performance.getLimitAge())
                .castMembers(performance.getCastMembers()
                        .stream()
                        .map(CastMemberDto::from)
                        .toList())
                .entertainment(performance.getEntertainment())
                .build();
    }
}
