package muse_kopis.muse.member;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long signUp(
            String username,
            String password,
            String name
    ) {
        MemberValidator memberValidator = new MemberValidator(memberRepository);
        String encodedPassword = PasswordEncoder.hashPassword(password);
        Member member = new Member(username, encodedPassword, name);
        member.signUp(memberValidator);
        return memberRepository.save(member).getId();
    }

    public Long login(String username, String password) {
        Member member = memberRepository.getByUsername(username);
        return member.login(password);
    }
}
