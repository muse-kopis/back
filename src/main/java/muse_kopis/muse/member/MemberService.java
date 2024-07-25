package muse_kopis.muse.member;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.common.ConfilctException;
import muse_kopis.muse.common.UnAuthorizationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public String signUp(
            String memberId,
            String password,
            String name
    ) {
        memberRepository.findByMemberId(memberId).ifPresent(it -> {
                    throw new ConfilctException("이미 존재하는 회원입니다.");
        });

        memberRepository.save(
                Member.builder()
                        .memberId(memberId)
                        .password(password)
                        .name(name)
                .build());
        return memberId;
    }

    public String login(String memberId, String password) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UnAuthorizationException("존재하지 않는 회원입니다."));
        member.login(password);
        return member.getMemberId();
    }
}
