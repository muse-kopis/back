package muse_kopis.muse.heart.domain;

import java.util.List;
import java.util.Optional;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByOauthMemberAndPerformance(OauthMember oauthMember, Performance performance);
    List<Heart> findByOauthMember(OauthMember member);

    boolean existsByOauthMemberAndPerformance(OauthMember oauthMember, Performance performance);
}
