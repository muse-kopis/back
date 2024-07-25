package muse_kopis.muse.member;

public record SignUpRequest(
        String memberId,
        String password,
        String name
) {
}
