package muse_kopis.muse.member;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.common.member.DuplicateUsernameException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateSignUp(String username) {
        if(memberRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("이미 존재하는 아이디 입니다.");
        }
    }
}
