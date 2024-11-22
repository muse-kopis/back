package muse_kopis.muse.auth.oauth.presentation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.auth.dto.LoginResponse;
import muse_kopis.muse.auth.jwt.JwtService;
import muse_kopis.muse.auth.oauth.application.OauthService;
import muse_kopis.muse.auth.oauth.domain.OauthServerType;
import muse_kopis.muse.auth.oauth.domain.dto.LoginDto;
import muse_kopis.muse.auth.oauth.domain.dto.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final JwtService jwtService;

    /**
     * @apiNote AuthCode Redirect URL, AuthCode 얻기위한 URL redirect
     * @param OauthServerType oauthServerType
     * @return Void
     */
    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable("oauthServerType") OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        log.info(oauthServerType.name());
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        log.info(redirectUrl);
        return ResponseEntity.ok().build();
    }

    /**
     * @apiNote Login
     * @param OauthServerType oauthServerType
     * @param String code
     * @return LoginResponse
     */
    @GetMapping("/login/{oauthServerType}")
    public ResponseEntity<LoginResponse> login(
            @PathVariable("oauthServerType") OauthServerType oauthServerType,
            @RequestParam("code") String code
    ) {
        LoginDto login = oauthService.login(oauthServerType, code);
        String token = jwtService.createToken(login.id());
        log.info("token = {}", token);
        return ResponseEntity.ok().body(new LoginResponse(token, login.isNewUser()));
    }

    /**
     * @apiNote Update Username
     * @param Long memberId (JWT Token)
     * @param String username
     * @return Void
     */
    @PatchMapping("/username")
    public ResponseEntity<Void> updateUsername(@Auth Long memberId, @RequestParam String username) {
        oauthService.updateUsername(memberId, username);
        return ResponseEntity.ok().build();
    }

    /**
     * @apiNote Update Username
     * @param Long memberId (JWT Token)
     * @return UserInfo
     */
    @GetMapping("/mypage")
    public ResponseEntity<UserInfo> userInfo(@Auth Long memberId) {
        return ResponseEntity.ok().body(oauthService.getInfo(memberId));
    }

    /**
     * @apiNote Delete User
     * @param Long memberId (JWT Token)
     * @return Void
    */
    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(@Auth Long memberId) {
        oauthService.deleteUser(memberId);
        return ResponseEntity.ok().build();
    }
}