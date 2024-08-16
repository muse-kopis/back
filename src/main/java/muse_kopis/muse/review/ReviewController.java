package muse_kopis.muse.review;

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
                reviewRequest.star());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@Auth Long memberId, @ModelAttribute PerformanceInfo performanceInfo) {
        return ResponseEntity.ok().body(reviewService.getReviews(memberId, performanceInfo.performanceName(),
                performanceInfo.venue()));
    }
}
