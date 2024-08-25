package muse_kopis.muse.performance.usergenre;

import java.util.Optional;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.common.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {

    default UserGenre getUserGenreByOauthMember(OauthMember oauthMember) {
        return findByOauthMember(oauthMember)
                .orElseThrow(() -> new NotFoundMemberException("존재하지 않는 회원입니다."));
    }
    Optional<UserGenre> findByOauthMember(OauthMember oauthMember);
}
