package muse_kopis.muse.actor.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteActorRepository extends JpaRepository<FavoriteActor, Long> {

    List<FavoriteActor> findAllByMemberId(Long memberId);
}
