package muse_kopis.muse.performance.usergenre;

import java.util.Optional;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {

    Optional<UserGenre> findByOauthMember(OauthMember oauthMember);
}
