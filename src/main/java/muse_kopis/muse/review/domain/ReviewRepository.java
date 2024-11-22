package muse_kopis.muse.review.domain;

import java.util.List;
import muse_kopis.muse.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPerformance(Performance performance);
}
