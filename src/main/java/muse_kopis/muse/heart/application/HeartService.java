package muse_kopis.muse.heart.application;

import jakarta.transaction.Transactional;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;

import java.util.List;

public interface HeartService {

    @Transactional
    void like(Long memberId, Long performanceId);
    @Transactional
    void unlike(Long memberId, Long performanceId);
    @Transactional
    List<PerformanceResponse> getMembersLikePerformanceList(Long memberId);
    @Transactional
    boolean getMemberIsLikePerformance(Long memberId, Long performanceId);
}
