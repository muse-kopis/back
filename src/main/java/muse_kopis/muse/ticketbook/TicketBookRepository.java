package muse_kopis.muse.ticketbook;

import java.util.List;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.common.NotFoundTicketBookException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketBookRepository extends JpaRepository<TicketBook, Long> {

    default TicketBook getByTicketBookId(Long ticketBookId) {
        return findById(ticketBookId).orElseThrow(() -> new NotFoundTicketBookException("티켓북을 찾을 수 없습니다."));
    }
    List<TicketBook> findAllByOauthMember(OauthMember oauthMember);
}
