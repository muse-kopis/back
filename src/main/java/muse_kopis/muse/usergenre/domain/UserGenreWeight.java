package muse_kopis.muse.usergenre.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.genre.domain.GenreType;

@Entity
@Getter
@NoArgsConstructor
public class UserGenreWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GenreType genreType;

    private Integer weight = 0;

    @ManyToOne
    @JoinColumn(name = "user_genre_id")
    private UserGenre userGenre;

    public UserGenreWeight(GenreType genreType, Integer weight, UserGenre userGenre) {
        this.genreType = genreType;
        this.weight = weight;
        this.userGenre = userGenre;
    }

    public void incrementWeight() {
        this.weight++;
    }
}