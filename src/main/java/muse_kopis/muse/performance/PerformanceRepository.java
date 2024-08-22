package muse_kopis.muse.performance;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import muse_kopis.muse.common.NotFoundPerformanceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    default Performance getByPerformanceId(Long performanceId) {
        return findById(performanceId)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
    }
    List<Performance> findAllByPerformanceNameContains(String search);
    List<Performance> findByPerformanceName(String performanceName);
    List<Performance> findAllByState(String state);
    List<Performance> findAllByStateOrState(String currentPerformances, String upcomingPerformances);
    Optional<Performance> findByPerformanceNameAndVenue(String performanceName, String venue);
}
