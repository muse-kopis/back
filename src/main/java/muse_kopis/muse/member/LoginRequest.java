package muse_kopis.muse.member;

public record LoginRequest(
        String memberId,
        String password
) {
}
