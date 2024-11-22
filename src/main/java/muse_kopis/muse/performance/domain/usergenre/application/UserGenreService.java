package muse_kopis.muse.performance.domain.usergenre.application;

import jakarta.transaction.Transactional;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenre;

import java.util.List;

public interface UserGenreService {

    @Transactional
    void updateGenre(Performance performance, OauthMember oauthMember);
    @Transactional
    void updateGenres(List<Long> performanceIds, Long memberId);
    @Transactional
    UserGenre initGenre(OauthMember oauthMember);
    @Transactional
    List<PerformanceResponse> showOnboarding();
}
