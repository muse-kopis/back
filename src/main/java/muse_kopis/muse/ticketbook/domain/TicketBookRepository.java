package muse_kopis.muse.ticketbook.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.common.ticketbook.NotFoundTicketBookException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketBookRepository extends JpaRepository<TicketBook, Long> {

    default TicketBook getByTicketBookId(Long ticketBookId) {
        return findById(ticketBookId).orElseThrow(() -> new NotFoundTicketBookException("티켓북을 찾을 수 없습니다."));
    }
    default TicketBook getByIdentifier(String identifier) {
        return findByIdentifier(identifier).orElseThrow(() -> new NotFoundTicketBookException("티켓북을 찾을 수 없습니다."));
    }
    Optional<TicketBook> findByIdentifier(String identifier);
    List<TicketBook> findAllByOauthMember(OauthMember oauthMember);
    @Query("SELECT tb FROM TicketBook tb WHERE tb.oauthMember.id = :oauthId and tb.viewDate BETWEEN :startDate AND :endDate")
    List<TicketBook> findByOauthMemberAndViewDate(@Param("oauthId")Long oauthId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    long countTicketBookByOauthMember(OauthMember oauthMember);
    List<TicketBook> findAllByIdentifier(String identifier);
}
