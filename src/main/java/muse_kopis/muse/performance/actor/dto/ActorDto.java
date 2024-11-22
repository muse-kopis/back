package muse_kopis.muse.performance.actor.dto;

import lombok.Builder;
import muse_kopis.muse.performance.actor.domain.Actor;

@Builder
public record ActorDto(
        String name
) {
    public static ActorDto from(Actor actor) {
        return ActorDto.builder()
                .name(actor.getName())
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
