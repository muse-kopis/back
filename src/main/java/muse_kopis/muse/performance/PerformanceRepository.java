package muse_kopis.muse.performance;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<List<Performance>> findAllByPerformanceNameContains(String search);
}