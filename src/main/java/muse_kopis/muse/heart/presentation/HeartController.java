package muse_kopis.muse.heart.presentation;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.heart.application.HeartService;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class HeartController {

    private final HeartService heartService;

    /**
     * @apiNote Performance Like
     * @param Long memberId (JWT Token)
     * @param Long performanceId
     * @return Void
     */
    @Operation(summary = "관심 공연 지정",
            description = "관심 공연으로 지정합니다. 사용자-공연 연결 및 사용자의 가중치 테이블을 업데이트 합니다.")
    @PostMapping("/{performanceId}/like")
    public ResponseEntity<Void> like(@Auth Long memberId, @PathVariable("performanceId") Long performanceId) {
        log.info("memberId = {}", memberId);
        heartService.like(memberId, performanceId);
        return ResponseEntity.ok().build();
    }

    /**
     * @apiNote Performance Unlike
     * @param Long memberId (JWT Token)
     * @param Long performanceId
     * @return Void
     */
    @Operation(summary = "관심 공연 해제",
            description = "관심 공연을 해제합니다. 사용자-공연 연결을 제거합니다. 사용자 가중치 테이블은 업데이트 하지 않습니다.")
    @DeleteMapping("/{performanceId}/like")
    public ResponseEntity<Void> unlike(@Auth Long memberId, @PathVariable("performanceId") Long performanceId) {
        heartService.unlike(memberId, performanceId);
        return ResponseEntity.ok().build();
    }

    /**
     * @apiNote Performance Like List
     * @param Long memberId (JWT Token)
     * @return List<PerformanceResponse>
     */
    @Operation(summary = "사용자 관심 공연 리스트",
            description = "사용자가 관심 공연으로 지정한 공연들의 리스트를 조회합니다.")
    @GetMapping("/like")
    public ResponseEntity<List<PerformanceResponse>> getPerformances(@Auth Long memberId) {
        return ResponseEntity.ok().body(heartService.getMembersLikePerformanceList(memberId));
    }

    /**
     * @apiNote Check Performance Like
     * @param Long memberId (JWT Token)
     * @param Long performanceId
     * @return Boolean
     */
    @Operation(summary = "사용자 관심 공연 조회",
            description = "해당 공연이 사용자가 관심 공연으로 지정한 공연인지 조회합니다.")
    @GetMapping("/{performanceId}/like")
    public ResponseEntity<Boolean> getPerformance(@Auth Long memberId, @PathVariable("performanceId") Long performanceId) {
        return ResponseEntity.ok().body(heartService.getMemberIsLikePerformance(memberId, performanceId));
    }
}
