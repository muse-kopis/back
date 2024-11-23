package muse_kopis.muse.review.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.review.domain.Review;

@Schema
@Builder
public record ReviewResponse(
        String username,
        String content,
        Integer star,
        String castMembers,
        Boolean visible
) {
    public static ReviewResponse from(Review review, String castMembers) {
        return ReviewResponse.builder()
                .username(review.getOauthMember().username())
                .content(review.getContent())
                .star(review.getStar())
                .visible(review.getVisible())
                .castMembers(castMembers)
                .build();
    }

    public static ReviewResponse from(OauthMember oauthMember, Integer star, String content, Boolean visible, String castMembers) {
        return ReviewResponse.builder()
                .star(star)
                .content(content)
                .visible(visible)
                .username(oauthMember.username())
                .castMembers(castMembers)
                .build();
    }

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .username(review.getOauthMember().username())
                .content(review.getContent())
                .star(review.getStar())
                .visible(review.getVisible())
                .build();
    }
}
