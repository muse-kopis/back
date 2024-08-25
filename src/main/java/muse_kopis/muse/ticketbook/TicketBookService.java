package muse_kopis.muse.ticketbook;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.auth.oauth.domain.UserTier;
import muse_kopis.muse.common.InvalidLocalDateException;
import muse_kopis.muse.common.NotFoundTicketBookException;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
import muse_kopis.muse.performance.usergenre.UserGenreService;
import muse_kopis.muse.review.dto.ReviewResponse;
import muse_kopis.muse.ticketbook.dto.TicketBookResponse;
import muse_kopis.muse.ticketbook.photo.Photo;
import muse_kopis.muse.ticketbook.photo.PhotoRepository;
import muse_kopis.muse.ticketbook.photo.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketBookService {

    private final OauthMemberRepository oauthMemberRepository;
    private final TicketBookRepository ticketBookRepository;
    private final PerformanceRepository performanceRepository;
    private final PhotoRepository photoRepository;
    private final UserGenreService userGenreService;
    private final PhotoService photoService;

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
    public Long writeTicketBook(
            Long memberId,
            Long performanceId,
            LocalDateTime viewDate,
            List<String> urls,
            Integer star,
            String content,
            Boolean visible,
            String castMembers
    ) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        ReviewResponse reviewResponse = ReviewResponse.from(oauthMember, performance, star, content, visible, castMembers);
        TicketBook ticketBook = ticketBookRepository.save(TicketBook.from(oauthMember, viewDate, reviewResponse, performance));
        photoRepository.saveAll(validPhotos(urls, ticketBook));
        userGenreService.updateGenre(performance, oauthMember);
        tierUpdate(oauthMember);
        return ticketBook.getId();
    }

    private void tierUpdate(OauthMember oauthMember) {
        long counted = ticketBookRepository.countTicketBookByOauthMember(oauthMember);
        oauthMember.updateUserTier(UserTier.fromCount(counted));
        oauthMemberRepository.save(oauthMember);
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
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        TicketBook ticketBook = ticketBookRepository.getByTicketBookId(ticketBookId);
        ticketBook.validate(oauthMember);
        photoRepository.findAllByTicketBook(ticketBook).forEach(photo -> photoService.deleteImageFromS3(photo.getUrl()));
        photoRepository.deleteAll(photoRepository.findAllByTicketBook(ticketBook));
        ticketBookRepository.delete(ticketBook);
        return ticketBookId;
    }

    @Transactional
    public TicketBookResponse ticketBookInDate(Long memberId, LocalDate localDate) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.plusDays(1).atStartOfDay();
        TicketBook ticketBook = ticketBookRepository.findByOauthMemberAndViewDate(memberId, startDateTime, endDateTime)
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
        LocalDateTime startDate = yearMonth.atDay(1).atTime(0,0,0);
        LocalDateTime endDate = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();
        List<TicketBook> ticketBooks = ticketBookRepository.findAllByOauthMemberAndViewDateBetween(memberId, startDate, endDate);
        return ticketBooks.stream()
                .map(ticketBook -> {
                            List<Photo> photos = photoRepository.findAllByTicketBook(ticketBook);
                            return TicketBookResponse.from(ticketBook, photos);
                        })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateTicketBook(
            Long memberId,
            Long ticketBookId,
            LocalDateTime viewDate,
            List<String> urls,
            Integer star,
            String content,
            Boolean visible,
            String castMembers
    ) {
        TicketBook ticketBook = ticketBookRepository.getByTicketBookId(ticketBookId);
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        ticketBook.validate(oauthMember);
        List<Photo> list = validPhotos(urls, ticketBook);
        ReviewResponse review = ReviewResponse.from(oauthMember, ticketBook.getReview().getPerformance(), star,
                content, visible, castMembers);
        photoRepository.saveAll(list);
        ticketBook.update(viewDate, review);
        return ticketBookRepository.save(ticketBook).getId();
    }

    private List<Photo> validPhotos(List<String> urls, TicketBook ticketBook) {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        return urls.stream().map(url -> new Photo(url, ticketBook)).toList();
    }
}
