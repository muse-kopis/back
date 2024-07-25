package muse_kopis.muse.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.common.UnAuthorizationException;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;
    private String password;
    private String name;

    public void login(String password) {
        if (this.password.equals(password)) {
            return;
        }
        throw new UnAuthorizationException("비밀번호가 일치하지 않습니다.");
    }
}
