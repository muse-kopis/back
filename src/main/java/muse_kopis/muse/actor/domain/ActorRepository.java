package muse_kopis.muse.actor.domain;

import java.util.List;
import muse_kopis.muse.actor.domain.dto.FavoriteActorDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<FavoriteActorDto> findByName(String actorName);
}
