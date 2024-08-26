package muse_kopis.muse.review;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.PerformanceRepository;
import muse_kopis.muse.review.dto.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PerformanceRepository performanceRepository;
    private final ReviewRepository reviewRepository;
    private final OauthMemberRepository oauthMemberRepository;

    @Transactional
    public Review writeReview(Long memberId, String performanceName, String venue, String content, Integer star, Boolean visible) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceNameAndVenue(performanceName, venue);
        return reviewRepository.save(new Review(oauthMember, performance, content, star, visible));
    }

    @Transactional
    public List<ReviewResponse> getPublicReviews(Long memberId, String performanceName, String venue) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceNameAndVenue(performanceName, venue);
        List<Review> reviews = reviewRepository.findAllByPerformance(performance).stream()
                .filter(Review::isVisible)
                .toList();
        return reviews.stream().map(ReviewResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewResponse> getPrivateReview(Long memberId, String performanceName, String venue) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceNameAndVenue(performanceName, venue);
        List<Review> reviews = reviewRepository.findAllByPerformance(performance);
        return reviews.stream().map(ReviewResponse::from).collect(Collectors.toList());
    }
}
