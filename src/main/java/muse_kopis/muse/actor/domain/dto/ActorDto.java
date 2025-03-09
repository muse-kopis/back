package muse_kopis.muse.actor.domain.dto;

import lombok.Builder;
import muse_kopis.muse.actor.domain.FavoriteActor;

@Builder
public record ActorDto(
        String name,
        String actorId
) {
    public static ActorDto from(FavoriteActor actor) {
        return ActorDto.builder()
                .name(actor.getActor().getName())
                .actorId(actor.getActor().getActorId())
                .build();
    }
}
