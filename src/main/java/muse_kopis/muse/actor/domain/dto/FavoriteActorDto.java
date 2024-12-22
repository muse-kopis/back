package muse_kopis.muse.actor.domain.dto;

import lombok.Builder;
import muse_kopis.muse.actor.domain.Actor;

@Builder
public record FavoriteActorDto(
        String name,
        String actorId
) {
    public static FavoriteActorDto from(Actor actor) {
        return FavoriteActorDto.builder()
                .name(actor.getName())
                .actorId(actor.getActorId())
                .build();
    }
}
