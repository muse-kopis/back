package muse_kopis.muse.review;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.NotFoundMemberException;
import muse_kopis.muse.common.NotFoundPerformanceException;
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

    public void writeReviews(Long memberId, String performanceName, String venue, String content, Integer star, Boolean visible) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        reviewRepository.save(new Review(oauthMember, performance, content, star, visible));
    }

    public List<ReviewResponse> getReviews(Long memberId, String performanceName, String venue) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        Performance performance = performanceRepository.findByPerformanceNameAndVenue(performanceName, venue)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾을 수 없습니다."));
        List<Review> reviews = reviewRepository.findAllByPerformance(performance);
        return reviews.stream().map(ReviewResponse::from).collect(Collectors.toList());
    }
}
