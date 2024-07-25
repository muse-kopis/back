package muse_kopis.muse.member;

public record SignUpRequest(
        String name,
        String memberId,
        String password
) {
}
