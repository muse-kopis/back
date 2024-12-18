package muse_kopis.muse.genre.domain.dto;

import java.util.List;
import muse_kopis.muse.genre.domain.GenreType;

public record PerformanceGenreInfo(
        List<GenreType> genres,
        Long performanceId
) {
}
