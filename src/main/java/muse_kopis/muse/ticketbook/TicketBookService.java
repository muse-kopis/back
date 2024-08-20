package muse_kopis.muse.ticketbook;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.InvalidLocalDateException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketBookService {

    private final OauthMemberRepository oauthMemberRepository;
    private final TicketBookRepository ticketBookRepository;
    private final PerformanceRepository performanceRepository;
    private final PhotoRepository photoRepository;
    private final UserGenreService userGenreService;

    @Transactional
    public List<TicketBookResponse> ticketBooks(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        return ticketBookRepository.findAllByOauthMember(oauthMember)
                .stream()
                .map(ticketBook -> {
                        List<Photo> photos = photoRepository.findAllByTicketBook(ticketBook);
                        return TicketBookResponse.from(ticketBook, photos);
                    }
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public Long writeTicketBook(Long memberId, Long performanceId, LocalDate viewDate, List<PhotoDto> photos, ReviewResponse review) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        TicketBook ticketBook = ticketBookRepository.save(TicketBook.from(oauthMember, viewDate, review, performance));
        List<Photo> list = photos.stream().map(photo -> new Photo(photo.url(), ticketBook)).toList();
        photoRepository.saveAll(list);
        userGenreService.updateGenre(performance, oauthMember);
        return ticketBook.getId();
    }

    @Transactional
    public TicketBookResponse ticketBook(Long memberId, Long ticketBookId) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        TicketBook ticketBook = ticketBookRepository.getByTicketBookId(ticketBookId);
        List<Photo> photos = photoRepository.findAllByTicketBook(ticketBook);
        return TicketBookResponse.from(ticketBook, photos);
    }

    @Transactional
    public Long deleteTicketBook(Long memberId, Long ticketBookId) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        TicketBook ticketBook = ticketBookRepository.getByTicketBookId(ticketBookId);
        photoRepository.deleteAll(photoRepository.findAllByTicketBook(ticketBook));
        ticketBookRepository.delete(ticketBook);
        return ticketBookId;
    }

    @Transactional
    public TicketBookResponse ticketBookInDate(Long memberId, LocalDate localDate) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        TicketBook ticketBook = ticketBookRepository.findByOauthMemberAndAndViewDate(oauthMember, localDate)
                .orElseThrow(() -> new NotFoundTicketBookException("티켓북이 존재하지 않습니다."));
        List<Photo> photos = photoRepository.findAllByTicketBook(ticketBook);
        return TicketBookResponse.from(ticketBook, photos);
    }

    @Transactional
    public List<TicketBookResponse> ticketBooksForMonth(Long memberId, Integer year, Integer month) {
        if (year == null || month == null) {
           throw new InvalidLocalDateException("년도 또는 달이 입력되지 않았습니다.");
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<TicketBook> ticketBooks = ticketBookRepository.findAllByOauthMemberAndViewDateBetween(memberId, startDate, endDate);
        return ticketBooks.stream()
                .map(ticketBook -> {
                            List<Photo> photos = photoRepository.findAllByTicketBook(ticketBook);
                            return TicketBookResponse.from(ticketBook, photos);
                        })
                .collect(Collectors.toList());
    }
}
