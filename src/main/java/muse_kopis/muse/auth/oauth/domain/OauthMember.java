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
    private String nickname;
    private String profileImageUrl;

    public Long id() {
        return id;
    }

    public OauthId oauthId() {
        return oauthId;
    }

    public String nickname() {
        return nickname;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }
}