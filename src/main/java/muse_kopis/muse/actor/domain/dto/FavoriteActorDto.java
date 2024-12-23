package muse_kopis.muse.actor.domain.dto;

import lombok.Builder;
import muse_kopis.muse.actor.domain.FavoriteActor;

@Builder
public record FavoriteActorDto(
        String name,
        String actorId
) {
    public static FavoriteActorDto from(FavoriteActor actor) {
        return FavoriteActorDto.builder()
                .name(actor.getActor().getName())
                .actorId(actor.getActor().getActorId())
                .build();
    }
}
