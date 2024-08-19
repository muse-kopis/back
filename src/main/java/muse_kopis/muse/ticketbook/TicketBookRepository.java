package muse_kopis.muse.ticketbook;

import java.util.List;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketBookRepository extends JpaRepository<TicketBook, Long> {
    List<TicketBook> findAllByOauthMember(OauthMember oauthMember);
}
