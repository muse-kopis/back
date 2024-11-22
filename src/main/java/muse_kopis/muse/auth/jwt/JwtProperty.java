package muse_kopis.muse.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperty(
        String secretKey,
        Long accessTokenExpirationDay
) {
}
