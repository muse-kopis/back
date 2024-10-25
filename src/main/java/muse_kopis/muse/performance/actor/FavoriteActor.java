package muse_kopis.muse.performance.actor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import muse_kopis.muse.performance.usergenre.UserGenre;

@Entity
@NoArgsConstructor
public class FavoriteActor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_genre_id")
    private UserGenre userGenre;
    private String actorName;

    public FavoriteActor(UserGenre userGenre, String actorName) {
        this.userGenre = userGenre;
        this.actorName = actorName;
    }

    public Long id() {
        return id;
    }
}
