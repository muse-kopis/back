package muse_kopis.muse.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.castmember.CastMember;
import muse_kopis.muse.performance.castmember.dto.CastMemberDto;
import muse_kopis.muse.review.Review;

@Schema
@Builder
public record ReviewResponse(
        String userName,
        String content,
        Integer star,
        List<CastMemberDto> castMembers,
        Boolean visible
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .userName(review.getOauthMember().username())
                .content(review.getContent())
                .star(review.getStar())
                .castMembers(review.getPerformance().getCastMembers()
                        .stream()
                        .map(CastMemberDto::from)
                        .toList())
                .visible(review.getVisible())
                .build();
    }

    public static ReviewResponse from(OauthMember oauthMember, Performance performance, Integer star, String content, Boolean visible, String castMembers) {
        return ReviewResponse.builder()
                .star(star)
                .content(content)
                .visible(visible)
                .userName(oauthMember.username())
                .castMembers(Arrays.stream(castMembers.split("[,\\s]+"))
                        .map(String::trim)
                        .map(name -> name.endsWith("등") ? name.substring(0, name.length() - 1).trim() : name)
                        .filter(name -> !name.isEmpty())
                        .map(name -> CastMemberDto.from(new CastMember(name, performance))).toList())
                .build();
    }
}
