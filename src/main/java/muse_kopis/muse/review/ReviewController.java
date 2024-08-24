package muse_kopis.muse.review;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.review.dto.PerformanceInfo;
import muse_kopis.muse.review.dto.ReviewRequest;
import muse_kopis.muse.review.dto.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public ResponseEntity<Void> writeReviews(@Auth Long memberId, @RequestBody ReviewRequest reviewRequest) {
        reviewService.writeReviews(
                memberId,
                reviewRequest.performanceName(),
                reviewRequest.venue(),
                reviewRequest.content(),
                reviewRequest.star(),
                reviewRequest.visible()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(description = "공연상세 페이지 후기란에서 확인 할 수 있는 리뷰")
    @GetMapping("/public")
    public ResponseEntity<List<ReviewResponse>> getPublicReviews(@Auth Long memberId, @ModelAttribute PerformanceInfo performanceInfo) {
        return ResponseEntity.ok().body(reviewService.getPublicReviews(memberId, performanceInfo.performanceName(),
                performanceInfo.venue()));
    }

    @Operation(description = "사용자가 작성한 리뷰 전체")
    @GetMapping("/private")
    public ResponseEntity<List<ReviewResponse>> getPrivateReviews(@Auth Long memberId, @ModelAttribute PerformanceInfo performanceInfo) {
        return ResponseEntity.ok().body(reviewService.getPrivateReview(memberId, performanceInfo.performanceName(),
                performanceInfo.venue()));
    }
}
