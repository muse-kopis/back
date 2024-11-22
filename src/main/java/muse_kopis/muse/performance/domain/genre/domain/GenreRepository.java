package muse_kopis.muse.performance.domain.genre.domain;

import java.util.List;

import muse_kopis.muse.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByPerformance(Performance performance);
    List<Genre> findAllByGenre(GenreType genreType);
}
