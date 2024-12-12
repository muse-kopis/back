package muse_kopis.muse.genre.application;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.genre.domain.Genre;
import muse_kopis.muse.genre.domain.GenreRepository;
import muse_kopis.muse.genre.domain.GenreType;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final PerformanceRepository performanceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void saveGenre(String performanceName, GenreType genreType) {
        List<Performance> performances = performanceRepository.findByPerformanceName(performanceName);
        for (Performance performance : performances) {
            genreRepository.save(new Genre(performance, genreType));
        }
    }

    public void saveGenre(Long performanceId, List<GenreType> genreTypes) {
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        genreTypes.forEach(genreType -> genreRepository.save(new Genre(performance, genreType)));
        eventPublisher.publishEvent(performance);
    }

    @EventListener
    @Transactional
    public void updatePerformanceGenre(Performance performance) {
        GenreType genreType = genreRepository.mostFrequentGenre(performance);
        performance.updateGenre(genreType);
        performanceRepository.save(performance);
    }
}
