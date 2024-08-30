package muse_kopis.muse.auth.oauth.domain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum UserTier {
    MANIA, LOVER, NEWBIE;

    public static UserTier fromCount(long count) {
        if (count >= 20) {
            return MANIA;
        } else if (count >= 10) {
            return LOVER;
        } else {
            return NEWBIE;
        }
    }
}
