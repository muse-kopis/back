package muse_kopis.muse.performance.genre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import muse_kopis.muse.performance.Performance;

@Entity
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Performance performance;
    private GenreType genre;

    public Genre(Performance performance, GenreType genre) {
        this.performance = performance;
        this.genre = genre;
    }
}
