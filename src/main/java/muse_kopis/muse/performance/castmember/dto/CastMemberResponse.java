package muse_kopis.muse.performance.castmember.dto;

import lombok.Builder;
import muse_kopis.muse.performance.castmember.CastMember;

@Builder
public record CastMemberResponse(
        String name
) {
    public static CastMemberResponse from(CastMember castMember) {
        return CastMemberResponse.builder()
                .name(castMember.getName())
                .build();
    }
}
