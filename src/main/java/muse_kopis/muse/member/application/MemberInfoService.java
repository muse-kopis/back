package muse_kopis.muse.member.application;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.member.domain.MemberInfo;
import muse_kopis.muse.member.domain.MemberInfoRepository;
import muse_kopis.muse.member.domain.dto.MemberInfoRequest;
import muse_kopis.muse.member.domain.dto.MemberInfoResponse;
import muse_kopis.muse.usergenre.domain.UserGenre;
import muse_kopis.muse.usergenre.domain.UserGenreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final OauthMemberRepository oauthMemberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final UserGenreRepository userGenreRepository;

    public MemberInfoResponse getMemberInfo(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        MemberInfo memberInfo = memberInfoRepository.findByOauthMember(oauthMember);
        return MemberInfoResponse.from(memberInfo);
    }

    public void createMemberInfo(Long memberId, MemberInfoRequest memberInfoRequest) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        UserGenre userGenre = userGenreRepository.getUserGenreByOauthMember(oauthMember);
        memberInfoRepository.save(MemberInfo.builder()
                        .sex(memberInfoRequest.sex())
                        .birth(memberInfoRequest.birth())
                        .address(memberInfoRequest.address())
                        .oauthMember(oauthMember)
                        .userGenre(userGenre)
                .build());
    }

    public void deleteMemberInfo(Long memberId) {
        memberInfoRepository.deleteById(memberId);
    }

    public void updateMemberInfo(Long memberId, MemberInfoRequest request) {
        MemberInfo memberInfo = memberInfoRepository.getMemberInfoById(memberId);
        memberInfo.reviseMemberInfo(request);
    }
}
