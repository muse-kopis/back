package muse_kopis.muse.auth.oauth.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tier")
public class TierImageURL {

    private String mania;
    private String lover;
    private String newbie;
}
