package muse_kopis.muse.review;

import java.util.List;
import muse_kopis.muse.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPerformance(Performance performance);
}
