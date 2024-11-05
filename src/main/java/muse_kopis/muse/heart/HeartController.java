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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/{performanceId}/like")
    public ResponseEntity<Void> like(@Auth Long memberId, @PathVariable Long performanceId) {
        log.info("memberId = {}", memberId);
        heartService.like(memberId, performanceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{performanceId}/like")
    public ResponseEntity<Void> unlike(@Auth Long memberId, @PathVariable Long performanceId) {
        heartService.unlike(memberId, performanceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/like")
    public ResponseEntity<List<PerformanceResponse>> getPerformances(@Auth Long memberId) {
        return ResponseEntity.ok().body(heartService.getMembersLikePerformanceList(memberId));
    }

    @GetMapping("/{performanceId}/like")
    public ResponseEntity<Boolean> getPerformance(@Auth Long memberId, @PathVariable Long performanceId) {
        return ResponseEntity.ok().body(heartService.getMemberIsLikePerformance(memberId, performanceId));
    }
}
