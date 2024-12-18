package muse_kopis.muse.actor.domain.dto;

import lombok.Builder;
import muse_kopis.muse.actor.domain.Actor;
import muse_kopis.muse.actor.domain.CastMember;

@Builder
public record ActorDto(
        String actorId,
        String name,
        String role
) {
    public static ActorDto from(Actor actor) {
        return ActorDto.builder()
                .name(actor.getName())
                .actorId(actor.getActorId())
                .build();
    }

    public static ActorDto from(CastMember castMember) {
        return ActorDto.builder()
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
