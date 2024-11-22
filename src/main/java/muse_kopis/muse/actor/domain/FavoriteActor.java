package muse_kopis.muse.actor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.performance.domain.usergenre.domain.UserGenre;

@Getter
@Entity
@NoArgsConstructor
public class FavoriteActor extends Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "user_genre_id")  // 외래 키 매핑
    private UserGenre userGenre;

    public FavoriteActor(Long memberId, String actorName, UserGenre userGenre) {
        super(actorName);
        this.userGenre = userGenre;
        this.memberId = memberId;
    }

    public Long id() {
        return id;
    }
}
