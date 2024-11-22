package muse_kopis.muse.performance.domain.genre.application;

import java.util.List;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import muse_kopis.muse.performance.domain.genre.domain.Genre;
import muse_kopis.muse.performance.domain.genre.domain.GenreRepository;
import muse_kopis.muse.performance.domain.genre.domain.GenreType;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final PerformanceRepository performanceRepository;

    public GenreService(GenreRepository genreRepository, PerformanceRepository performanceRepository) {
        this.genreRepository = genreRepository;
        this.performanceRepository = performanceRepository;
    }

    public void saveGenre(String performanceName, GenreType genreType) {
        List<Performance> performances = performanceRepository.findByPerformanceName(performanceName);
        for (Performance performance : performances) {
            genreRepository.save(new Genre(performance, genreType));
        }
    }
}
