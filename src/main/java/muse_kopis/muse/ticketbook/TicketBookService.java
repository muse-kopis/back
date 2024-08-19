package muse_kopis.muse.ticketbook;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.NotFoundMemberException;
import muse_kopis.muse.common.NotFoundPerformanceException;
import muse_kopis.muse.common.NotFoundTicketBookException;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
import muse_kopis.muse.performance.usergenre.UserGenreService;
import muse_kopis.muse.review.dto.ReviewResponse;
import muse_kopis.muse.ticketbook.dto.PhotoDto;
import muse_kopis.muse.ticketbook.dto.TicketBookResponse;
import muse_kopis.muse.ticketbook.photo.Photo;
import muse_kopis.muse.ticketbook.photo.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketBookService {

    private final OauthMemberRepository oauthMemberRepository;
    private final TicketBookRepository ticketBookRepository;
    private final PerformanceRepository performanceRepository;
    private final PhotoRepository photoRepository;
    private final UserGenreService userGenreService;

    public List<TicketBookResponse> ticketBooks(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("사용자를 찾을 수 없습니다."));
        return ticketBookRepository.findAllByOauthMember(oauthMember)
                .stream()
                .map(TicketBookResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long writeTicketBook(Long memberId, Long performanceId, LocalDate viewDate, List<PhotoDto> photos, ReviewResponse review) {
        OauthMember oauthMember = oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("사용자를 찾을 수 없습니다."));
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        TicketBook ticketBook = ticketBookRepository.save(TicketBook.from(oauthMember, viewDate, review, performance));
        photoRepository.saveAll(photos.stream().map(photo -> new Photo(photo.url(), ticketBook)).toList());
        userGenreService.updateGenre(performance, oauthMember);
        return ticketBook.getId();
    }

    public TicketBookResponse ticketBook(Long memberId, Long ticketBookId) {
        oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("사용자를 찾을 수 없습니다."));
        return TicketBookResponse.from(ticketBookRepository.findById(ticketBookId)
                .orElseThrow(() -> new NotFoundTicketBookException("티켓북을 찾을 수 없습니다.")));
    }

    @Transactional
    public Long deleteTicketBook(Long memberId, Long ticketBookId) {
        oauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("사용자를 찾을 수 없습니다."));
        TicketBook ticketBook = ticketBookRepository.findById(ticketBookId)
                .orElseThrow(() -> new NotFoundTicketBookException("티켓북을 찾을 수 없습니다."));
        photoRepository.deleteAll(photoRepository.findAllByTicketBook(ticketBook));
        ticketBookRepository.delete(ticketBook);
        return ticketBookId;
    }
}
