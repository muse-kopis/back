package muse_kopis.muse.actor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.usergenre.domain.UserGenre;

@Getter
@Entity
@NoArgsConstructor
public class FavoriteActor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_genre_id")  // 외래 키 매핑
    private UserGenre userGenre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Actor actor;

    private Long memberId;

    public FavoriteActor(Long memberId, Actor actor, UserGenre userGenre) {
        this.userGenre = userGenre;
        this.memberId = memberId;
        this.actor = actor;
    }

    public Long id() {
        return id;
    }
}
