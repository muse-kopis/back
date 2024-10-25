package muse_kopis.muse.performance.actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteActorRepository extends JpaRepository<FavoriteActor, Long> {
}
