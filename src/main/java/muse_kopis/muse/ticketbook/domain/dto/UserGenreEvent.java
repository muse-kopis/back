package muse_kopis.muse.ticketbook.domain.dto;

public record UserGenreEvent(
        Long memberId,
        Long performanceId
) {
}
