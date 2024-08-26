package muse_kopis.muse.auth.oauth.domain;

public enum UserTier {
    NEWBIE, LOVER, MANIA
    ;

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
