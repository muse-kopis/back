package muse_kopis.muse.review.application;

import jakarta.transaction.Transactional;
import muse_kopis.muse.review.domain.Review;
import muse_kopis.muse.review.domain.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    @Transactional
    Review writeReview(Long memberId, String performanceName, String venue, String content, Integer star, Boolean visible);
    @Transactional
    List<ReviewResponse> getPublicReviews(Long memberId, Long performanceId);
    @Transactional
    List<ReviewResponse> getPrivateReview(Long memberId, Long performanceId);
}
