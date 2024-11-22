package muse_kopis.muse.review.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.review.application.ReviewService;
import muse_kopis.muse.review.application.ReviewServiceImpl;
import muse_kopis.muse.review.domain.Review;
import muse_kopis.muse.review.domain.dto.ReviewRequest;
import muse_kopis.muse.review.domain.dto.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * @apiNote Write Review
     * @param Long memberid
     * @param ReviewRequest reviewRequest
     * @return Long
     */
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.(해당 기능은 티켓북에서 쓰여서 따로 쓰이지 않습니다.)")
    @PostMapping
    public ResponseEntity<Long> writeReview(@Auth Long memberId, @RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.writeReview(
                memberId,
                reviewRequest.performanceName(),
                reviewRequest.venue(),
                reviewRequest.content(),
                reviewRequest.star(),
                reviewRequest.visible()
        );
        return ResponseEntity.ok().body(review.getId());
    }

    /**
     * @apiNote Performance Review
     * @param Long memberId
     * @param Long performanceId
     * @return List<ReviewResponse>
     */
    @GetMapping("/public/{performanceId}")
    @Operation(summary = "공연 리뷰",
            description = "공연상세 페이지 후기란에서 확인 할 수 있는 리뷰")
    public ResponseEntity<List<ReviewResponse>> getPublicReviews(@Auth Long memberId, @PathVariable Long performanceId) {
        return ResponseEntity.ok().body(reviewService.getPublicReviews(memberId, performanceId));
    }

    /**
     * @apiNote Performance Review Private
     * @param Long memberId
     * @param Long performanceId
     * @return List<ReviewResponse>
     */
    @GetMapping("/private/{performanceId}")
    @Operation(summary = "공연 리뷰(비공개 포함)",
            description = "사용자가 작성한 리뷰 전체")
    public ResponseEntity<List<ReviewResponse>> getPrivateReviews(@Auth Long memberId, @PathVariable Long performanceId) {
        return ResponseEntity.ok().body(reviewService.getPrivateReview(memberId, performanceId));
    }
}
