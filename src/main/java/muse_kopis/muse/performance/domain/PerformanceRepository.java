package muse_kopis.muse.performance.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import muse_kopis.muse.common.performance.NotFoundPerformanceException;
import muse_kopis.muse.genre.domain.GenreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    default Performance getByPerformanceId(Long performanceId) {
        return findById(performanceId)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
    }

    default Performance getByPerformanceNameAndVenue(String performanceName, String venue) {
        return findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
    }

    List<Performance> findAllByPerformanceNameContains(String search);
    List<Performance> findByPerformanceName(String performanceName);
    List<Performance> findAllByIdIn(List<Long> performanceIds);
    @Query("SELECT p FROM Performance p WHERE p.state = :state ORDER BY function('RAND')")
    List<Performance> findAllByState(@Param("state") String state);
    List<Performance> findAllByStateOrState(String currentPerformances, String upcomingPerformances);
    Optional<Performance> findByPerformanceNameAndVenue(String performanceName, String venue);
    @Query("SELECT p FROM Performance p WHERE :today BETWEEN p.startDate AND p.endDate")
    List<Performance> findPerformancesByDate(@Param("today") LocalDate today);
    List<Performance> findAllByGenreType(GenreType genreType);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Performance p " +
            "WHERE p.performanceName = :performanceName " +
            "AND p.venue = :venue " +
            "AND p.startDate = :startDate " +
            "AND p.endDate = :endDate")
    Boolean existsPerformance(@Param("performanceName") String performanceName,
                              @Param("venue") String venue,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);
}
