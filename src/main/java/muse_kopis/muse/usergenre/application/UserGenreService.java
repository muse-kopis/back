package muse_kopis.muse.usergenre.application;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.genre.domain.Genre;
import muse_kopis.muse.genre.domain.GenreRepository;
import muse_kopis.muse.ticketbook.domain.dto.UserGenreEvent;
import muse_kopis.muse.usergenre.domain.UserGenre;
import muse_kopis.muse.usergenre.domain.UserGenreRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGenreService {

    private final GenreRepository genreRepository;
    private final UserGenreRepository userGenreRepository;
    private final PerformanceRepository performanceRepository;
    private final OauthMemberRepository oauthMemberRepository;

    @EventListener
    @Transactional
    public void updateGenre(UserGenreEvent event) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(event.memberId());
        Performance performance = performanceRepository.getByPerformanceId(event.performanceId());
        List<Genre> genre = genreRepository.findAllByPerformance(performance);
        UserGenre userGenre = userGenreRepository.findByOauthMember(oauthMember)
                .orElseGet(() -> initGenre(oauthMember));
        genre.forEach(it -> userGenre.incrementGenreWeight(it.getGenre()));
    }

    @Transactional
    public void updateGenre(Long memberId, Long performanceId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        List<Genre> genre = genreRepository.findAllByPerformance(performance);
        UserGenre userGenre = userGenreRepository.findByOauthMember(oauthMember)
                .orElseGet(() -> initGenre(oauthMember));
        genre.forEach(it -> userGenre.incrementGenreWeight(it.getGenre()));
    }

    @Transactional
    public void updateGenres(Long memberId, List<Long> performanceIds) {
        performanceIds.forEach(performanceId -> updateGenre(memberId, performanceId));
    }

    @EventListener
    public UserGenre initGenre(OauthMember oauthMember) {
        return userGenreRepository.save(new UserGenre(oauthMember));
    }

    @Transactional
    public List<PerformanceResponse> showOnboarding() {
        List<Long> performanceIds = Arrays.asList(
                38L, 103L, 118L, 124L, 134L, 139L, 170L, 177L, 181L, 194L,
                202L, 243L, 252L, 255L, 260L, 280L, 300L, 305L, 324L, 395L,
                436L, 460L, 463L, 481L, 722L, 884L, 924L, 1000L, 1161L, 1235L
        );
        List<Performance> performances = performanceRepository.findAllByIdIn(performanceIds);
        return performances.stream().map(PerformanceResponse::from).toList();
    }
}
