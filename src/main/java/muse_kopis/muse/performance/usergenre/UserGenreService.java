package muse_kopis.muse.performance.usergenre;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import muse_kopis.muse.performance.genre.Genre;
import muse_kopis.muse.performance.genre.GenreRepository;
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
        List<Genre> genres = genreRepository.findAll().stream().limit(50).toList();
        List<Performance> performances = genres.stream()
                .map(genre -> performanceRepository.getByPerformanceId(genre.getPerformance().getId())).toList();
        return performances.stream().map(PerformanceResponse::from).toList();
    }
}
