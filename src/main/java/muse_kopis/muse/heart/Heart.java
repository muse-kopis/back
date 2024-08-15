package muse_kopis.muse.heart;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.member.Member;
import muse_kopis.muse.performance.Performance;

@Entity
@Getter
@NoArgsConstructor
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performance;
    @ManyToOne
    @JoinColumn(name = "auth_member_id")
    private OauthMember oauthMember;

    public Heart(OauthMember member, Performance performance) {
        this.oauthMember = member;
        this.performance = performance;
    }
}
