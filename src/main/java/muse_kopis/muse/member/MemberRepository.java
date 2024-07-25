package muse_kopis.muse.member;

import java.util.Optional;
import muse_kopis.muse.common.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다."));
    }

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
}
