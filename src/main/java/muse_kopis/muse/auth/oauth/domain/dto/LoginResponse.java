package muse_kopis.muse.auth.oauth.domain.dto;

public record LoginResponse(
        String token,
        Boolean isNewUser
) {
}