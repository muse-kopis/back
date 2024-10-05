package muse_kopis.muse.performance.genre;

import java.util.List;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
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
