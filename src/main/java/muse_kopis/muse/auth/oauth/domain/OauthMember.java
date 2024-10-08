package muse_kopis.muse.auth.oauth.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "oauth_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "oauth_id_unique",
                        columnNames = {
                                "oauth_server_id",
                                "oauth_server"
                        }
                ),
        }
)
public class OauthMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OauthId oauthId;
    private String nickname; // 카카오에서 받아온 사용자 이름
    private String profileImageUrl;
    private UserTier tier;
    private String tierImageUrl;
    private String username; // 서비스 내부에서 보이는 사용자 이름
    private Boolean isNewUser;

    public Long id() {
        return id;
    }

    public OauthId oauthId() {
        return oauthId;
    }

    public String nickname() {
        return nickname;
    }

    public String username() {
        return username;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }

    public String tierImageUrl() {
        return tierImageUrl;
    }

    public UserTier userTier() {
        return tier;
    }

    public Boolean isNewUser() {
        return isNewUser;
    }
    public void updateUserTier(UserTier tier, String url) {
        this.tier = tier;
        this.tierImageUrl = url;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateOldUser(Boolean isNewUser) {
        this.isNewUser = isNewUser;
    }
}