package muse_kopis.muse.member.dto;

public record SignUpRequest(
        String username,
        String password,
        String name
) {
}
