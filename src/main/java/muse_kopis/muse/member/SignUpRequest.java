package muse_kopis.muse.member;

public record SignUpRequest(
        String username,
        String password,
        String name
) {
}
