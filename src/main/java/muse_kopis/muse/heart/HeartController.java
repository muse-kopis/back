package muse_kopis.muse.heart;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.heart.dto.LikeRequest;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/performances/like")
    public ResponseEntity<Void> like(@Auth Long memberId, @RequestBody LikeRequest likeRequest) {
        log.info("memberId = {}", memberId);
        heartService.like(memberId, likeRequest.performanceName(), likeRequest.venue());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/performances/like")
    public ResponseEntity<Void> unlike(@Auth Long memberId, @RequestBody LikeRequest likeRequest) {
        heartService.unlike(memberId, likeRequest.performanceName(), likeRequest.venue());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/performances/like")
    public ResponseEntity<List<PerformanceResponse>> getPerformances(@Auth Long memberId) {
        return ResponseEntity.ok().body(heartService.getMembersLikePerformanceList(memberId));
    }
}
