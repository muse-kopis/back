package muse_kopis.muse.review.dto;

import java.util.List;
import lombok.Builder;
import muse_kopis.muse.performance.castmember.dto.CastMemberResponse;
import muse_kopis.muse.review.Review;

@Builder
public record ReviewResponse(
        String content,
        Integer star,
        List<CastMemberResponse> crews
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .content(review.getContent())
                .star(review.getStar())
                .crews(review.getPerformance().getCastMembers()
                        .stream()
                        .map(CastMemberResponse::from)
                        .toList())
                .build();
    }
}
