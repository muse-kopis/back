package muse_kopis.muse.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.stream.Collectors;
import lombok.Builder;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.castmember.CastMember;
import muse_kopis.muse.review.Review;

@Schema
@Builder
public record ReviewResponse(
        String username,
        String content,
        Integer star,
        String castMembers,
        Boolean visible
) {
    public static ReviewResponse from(Review review) {
        String castMembers = review.getPerformance().getCastMembers().stream()
                .map(CastMember::getName)
                .collect(Collectors.joining(", "));
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
}
