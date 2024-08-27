package muse_kopis.muse.performance.genre;

import java.util.List;
import java.util.Optional;
import muse_kopis.muse.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByPerformance(Performance performance);
    List<Genre> findAllByGenre(GenreType genreType);
}
