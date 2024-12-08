package muse_kopis.muse.usergenre.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.auth.oauth.application.OauthService;
import muse_kopis.muse.performance.domain.dto.Onboarding;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.usergenre.application.UserGenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
        log.info("{}", memberId);
        userGenreService.updateGenres(memberId, onboarding.performanceIds());
        return ResponseEntity.ok().body(username);
    }
}
