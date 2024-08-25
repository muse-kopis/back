package muse_kopis.muse.auth.oauth.application;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.auth.oauth.domain.OauthServerType;
import muse_kopis.muse.auth.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import muse_kopis.muse.auth.oauth.domain.client.OauthMemberClientComposite;
import muse_kopis.muse.auth.oauth.domain.dto.UserInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public Long login(OauthServerType oauthServerType, String authCode) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
        return saved.id();
    }

    public void updateUsername(Long memberId, String newUsername) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        oauthMember.updateUsername(newUsername);
        oauthMemberRepository.save(oauthMember);
    }

    public UserInfo getInfo(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        return new UserInfo(oauthMember.username(), oauthMember.userTier());
    }
}