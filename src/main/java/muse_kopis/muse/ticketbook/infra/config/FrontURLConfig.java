package muse_kopis.muse.ticketbook.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "front")
public record FrontURLConfig(
        String url
) {
}
