package muse_kopis.muse.performance.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.castmember.CastMember;
import muse_kopis.muse.performance.castmember.dto.CastMemberResponse;

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
        List<CastMemberResponse> castMembers,
        String entertainment
//        String ticketing,
//        String ticketingURL
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
                        .map(CastMemberResponse::from)
                        .toList())
                .entertainment(performance.getEntertainment())
                .build();
    }
}
