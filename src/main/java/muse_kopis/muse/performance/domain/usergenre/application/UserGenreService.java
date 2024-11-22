package muse_kopis.muse.performance.domain.usergenre.application;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.performance.domain.genre.domain.Genre;
import muse_kopis.muse.performance.domain.genre.domain.GenreRepository;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenre;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGenreService {

    private final GenreRepository genreRepository;
    private final UserGenreRepository userGenreRepository;
    private final PerformanceRepository performanceRepository;
    private final OauthMemberRepository oauthMemberRepository;

    @Transactional
    public void updateGenre(Performance performance, OauthMember oauthMember) {
        List<Genre> genre = genreRepository.findAllByPerformance(performance);
        UserGenre userGenre = userGenreRepository.findByOauthMember(oauthMember)
                .orElseGet(() -> initGenre(oauthMember));
        genre.forEach(it -> userGenre.incrementGenreWeight(it.getGenre()));
    }

    @Transactional
    public void updateGenres(List<Long> performanceIds, Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        List<Performance> performances = performanceIds.stream()
                .map(performanceRepository::getByPerformanceId).toList();
        performances.forEach(performance -> updateGenre(performance, oauthMember));
    }

    @Transactional
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
