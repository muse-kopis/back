package muse_kopis.muse.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.member.domain.dto.MemberInfoRequest;
import muse_kopis.muse.usergenre.domain.UserGenre;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private OauthMember oauthMember;

    @OneToOne
    private UserGenre userGenre;

    @Embedded
    private Address address;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt; // 생성 시간

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updatedAt; // 수정 시간

    private Sex sex;
    private LocalDate birth;

    public Sex sex() {
        return sex;
    }

    public LocalDate birth() {
        return birth;
    }

    public Address address() {
        return address;
    }

    public UserGenre userGenre() {
        return userGenre;
    }

    public void reviseMemberInfo(MemberInfoRequest request) {
        this.sex = request.sex();
        this.birth = request.birth();
        this.address = request.address();
    }
}
