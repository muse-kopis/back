package muse_kopis.muse.performance.usergenre;

import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.genre.Genre;
import muse_kopis.muse.performance.genre.GenreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGenreService {

    private final GenreRepository genreRepository;
    private final UserGenreRepository userGenreRepository;

    public void updateGenre(Performance performance, OauthMember oauthMember) {
        List<Genre> genre = genreRepository.findAllByPerformance(performance);
        UserGenre userGenre = userGenreRepository.findByOauthMember(oauthMember)
                .orElseGet(() -> initGenre(oauthMember));
        genre.forEach(it -> userGenre.incrementGenreWeight(it.getGenre()));
    }

    public UserGenre initGenre(OauthMember oauthMember) {
        return userGenreRepository.save(new UserGenre(oauthMember));
    }
}
