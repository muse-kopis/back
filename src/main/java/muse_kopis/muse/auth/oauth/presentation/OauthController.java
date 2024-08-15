package muse_kopis.muse.auth.oauth.presentation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import muse_kopis.muse.auth.jwt.JwtService;
import muse_kopis.muse.auth.oauth.application.OauthService;
import muse_kopis.muse.auth.oauth.domain.OauthServerType;
import muse_kopis.muse.member.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final JwtService jwtService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    } // AuthCode 얻기위한 URL redirect

    @GetMapping("/login/{oauthServerType}")
    public ResponseEntity<LoginResponse> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ) {
        Long id = oauthService.login(oauthServerType, code);
        String token = jwtService.createToken(id);
        return ResponseEntity.ok().body(new LoginResponse(token));
    }
}