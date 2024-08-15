package muse_kopis.muse.performance;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findAllByPerformanceNameContains(String search);
    List<Performance> findByPerformanceName(String performanceName);
    List<Performance> findAllByState(String state);
    Optional<Performance> findByPerformanceNameAndVenue(String performanceName, String venue);
}
