package muse_kopis.muse.actor.domain.dto;

import lombok.Builder;
import muse_kopis.muse.actor.domain.Actor;
import muse_kopis.muse.actor.domain.CastMember;

@Builder
public record CastMemberDto(
        String actorId,
        String name,
        String role
) {
    public static CastMemberDto from(CastMember castMember) {
        return CastMemberDto.builder()
                .name(castMember.getName())
                .actorId(castMember.getActorId())
                .role(castMember.getRole())
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
