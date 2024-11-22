package muse_kopis.muse.performance.domain.actor.domain;

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

    public Actor(String name) {
        this.name = name;
    }
}
