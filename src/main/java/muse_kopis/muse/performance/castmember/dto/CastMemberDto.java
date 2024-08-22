package muse_kopis.muse.performance.castmember.dto;

import lombok.Builder;
import muse_kopis.muse.performance.castmember.CastMember;

@Builder
public record CastMemberDto(
        String name
) {
    public static CastMemberDto from(CastMember castMember) {
        return CastMemberDto.builder()
                .name(castMember.getName())
                .build();
    }
}
