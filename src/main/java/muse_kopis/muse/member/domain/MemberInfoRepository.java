package muse_kopis.muse.member.domain;

import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.common.member.NotFoundMemberInfoException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

    default MemberInfo getMemberInfoById(Long memberId) {
        return findById(memberId).orElseThrow(() -> new NotFoundMemberInfoException("사용자 정보를 찾지 못했습니다."));
    }

    MemberInfo findByOauthMember(OauthMember oauthMember);
}
