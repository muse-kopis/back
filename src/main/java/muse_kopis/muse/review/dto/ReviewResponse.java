package muse_kopis.muse.review.dto;

import java.util.List;
import lombok.Builder;
import muse_kopis.muse.performance.castmember.dto.CastMemberDto;
import muse_kopis.muse.review.Review;

@Builder
public record ReviewResponse(
        String content,
        Integer star,
        List<CastMemberDto> castMembers,
        Boolean visible
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .content(review.getContent())
                .star(review.getStar())
                .castMembers(review.getPerformance().getCastMembers()
                        .stream()
                        .map(CastMemberDto::from)
                        .toList())
                .visible(review.getVisible())
                .build();
    }
}
