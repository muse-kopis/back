package muse_kopis.muse.review;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.review.dto.ReviewRequest;
import muse_kopis.muse.review.dto.ReviewResponse;
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

    @Operation(description = "공연상세 페이지 후기란에서 확인 할 수 있는 리뷰")
    @GetMapping("/public/{performanceId}")
    public ResponseEntity<List<ReviewResponse>> getPublicReviews(@Auth Long memberId, @PathVariable Long performanceId) {
        return ResponseEntity.ok().body(reviewService.getPublicReviews(memberId, performanceId));
    }

    @Operation(description = "사용자가 작성한 리뷰 전체")
    @GetMapping("/private/{performanceId}")
    public ResponseEntity<List<ReviewResponse>> getPrivateReviews(@Auth Long memberId, @PathVariable Long performanceId) {
        return ResponseEntity.ok().body(reviewService.getPrivateReview(memberId, performanceId));
    }
}
