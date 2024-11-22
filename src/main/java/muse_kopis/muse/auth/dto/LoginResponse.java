package muse_kopis.muse.auth.dto;

public record LoginResponse(
        String token,
        Boolean isNewUser
) {
}