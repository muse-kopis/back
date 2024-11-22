package muse_kopis.muse.review.application;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import muse_kopis.muse.performance.domain.actor.domain.CastMember;
import muse_kopis.muse.review.domain.Review;
import muse_kopis.muse.review.domain.ReviewRepository;
import muse_kopis.muse.review.domain.dto.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final PerformanceRepository performanceRepository;
    private final ReviewRepository reviewRepository;
    private final OauthMemberRepository oauthMemberRepository;

    public Review writeReview(Long memberId, String performanceName, String venue, String content, Integer star, Boolean visible) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceNameAndVenue(performanceName, venue);
        return reviewRepository.save(new Review(oauthMember, performance, content, star, visible));
    }

    public List<ReviewResponse> getPublicReviews(Long memberId, Long performanceId) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        List<Review> reviews = reviewRepository.findAllByPerformance(performance).stream()
                .filter(Review::isVisible)
                .toList();
        return reviews.stream().map(review -> ReviewResponse.from(review, performance.getCastMembers().stream().map(CastMember::toString)
                .collect(Collectors.joining(", ")))).collect(Collectors.toList());
    }

    public List<ReviewResponse> getPrivateReview(Long memberId, Long performanceId) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        List<Review> reviews = reviewRepository.findAllByPerformance(performance);
        return reviews.stream().map(review -> ReviewResponse.from(review, performance.getCastMembers().stream().map(CastMember::toString).collect(
                Collectors.joining(", ")))).collect(Collectors.toList());
    }
}
