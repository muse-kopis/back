package muse_kopis.muse.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.auth.PasswordEncoder;
import muse_kopis.muse.common.UnAuthorizationException;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;

    public Member(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public Long login(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.checkPassword(password, this.password)) {
            return id;
        }
        throw new UnAuthorizationException("비밀번호가 일치하지 않습니다.");
    }

    public void signUp(MemberValidator validator) {
        validator.validateSignUp(this.username);
    }
}
