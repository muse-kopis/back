package muse_kopis.muse.usergenre.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.auth.oauth.application.OauthService;
import muse_kopis.muse.performance.domain.dto.Onboarding;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.usergenre.application.UserGenreService;
import muse_kopis.muse.usergenre.domain.dto.UpdateInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/genre")
public class UserGenreController {

    private final UserGenreService userGenreService;
    private final OauthService oauthService;

    /**
     * @apiNote Performance Onboarding
     * @return List<PerformanceResponse>
     */
    @Operation(summary = "온보딩",
            description = "온보딩 화면을 보여줍니다.")
    @GetMapping("/onboarding")
    public ResponseEntity<List<PerformanceResponse>> showOnboarding() {
        return ResponseEntity.ok().body(userGenreService.showOnboarding());
    }

    /**
     * @apiNote Write Performance Onboarding
     * @param Long memberId (JWT Token)
     * @param Onboarding onboarding
     * @return String
     */
    @Operation(summary = "온보딩",
            description = "온보딩 내용을 등록합니다., 공연 아이디를 받아옵니다.")
    @PostMapping("/onboarding")
    public ResponseEntity<String> updateUserGenre(@Auth Long memberId, @RequestBody Onboarding onboarding) {
        String username = oauthService.updateUsername(memberId, onboarding.username());
        oauthService.updateUserState(memberId);
        userGenreService.updateGenres(onboarding.performanceId(), memberId);
        return ResponseEntity.ok().body(username);
    }

    /**
     * @apiNote Update Genre Table
     * @param Long memberId (JWT Token)
     * @param Long performanceId
     * @return Void
     */
    @Operation(summary = "가중치 테이블 갱신", description = "가중치 테이블을 장르 값에 따라 갱신합니다. 티켓북을 생성하거나, 관심 공연 지정 시 호출해주세요.")
    @PatchMapping
    public ResponseEntity<Void> updateGenre(@Auth Long memberId, @RequestBody UpdateInfo updateInfo) {
        userGenreService.updateGenre(memberId, updateInfo.performanceId());
        return ResponseEntity.ok().build();
    }
}
