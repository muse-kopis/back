package muse_kopis.muse.review.dto;

import lombok.Builder;
import muse_kopis.muse.review.Review;

@Builder
public record ReviewResponse(
        String content,
        Integer star,
        String crews
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .content(review.getContent())
                .star(review.getStar())
                .crews(review.getPerformance().getPerformanceCrews())
                .build();
    }
}
