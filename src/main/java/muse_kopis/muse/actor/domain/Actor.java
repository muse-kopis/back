package muse_kopis.muse.actor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public class Actor {

    @Column(name = "actor_name")
    private String name;
    private String actorId;

    public Actor(String name, String actorId) {
        this.name = name;
        this.actorId = actorId;
    }
}
