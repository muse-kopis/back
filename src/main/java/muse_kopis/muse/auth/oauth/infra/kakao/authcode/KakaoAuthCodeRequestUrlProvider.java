package muse_kopis.muse.auth.oauth.infra.kakao.authcode;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthServerType;
import muse_kopis.muse.auth.oauth.domain.authcode.AuthCodeRequestUrlProvider;
import muse_kopis.muse.auth.oauth.infra.kakao.KakaoOauthConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthConfig.clientId())
                .queryParam("redirect_uri", kakaoOauthConfig.redirectUri())
                .queryParam("scope", String.join(",", kakaoOauthConfig.scope()))
                .toUriString();
    }
}
