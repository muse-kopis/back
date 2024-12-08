package muse_kopis.muse.performance.domain.dto;

import java.util.List;

public record Onboarding(
        List<Long> performanceIds,
        String username
) {
}
