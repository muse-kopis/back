package muse_kopis.muse.auth.oauth.domain.dto;

import muse_kopis.muse.auth.oauth.domain.UserTier;

public record UserInfo(
        String username,
        UserTier userTier,
        String tierImageUrl
) {
}
