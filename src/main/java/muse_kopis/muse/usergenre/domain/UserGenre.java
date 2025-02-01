package muse_kopis.muse.usergenre.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.NoArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.genre.domain.GenreType;

@Entity
@NoArgsConstructor
public class UserGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "userGenre", cascade = CascadeType.ALL)
    private List<UserGenreWeight> genreWeights = new ArrayList<>();

    @OneToOne
    private OauthMember oauthMember;

    @Enumerated(EnumType.STRING)
    private GenreType favorite;

    @Enumerated(EnumType.STRING)
    private GenreType second;

    @Enumerated(EnumType.STRING)
    private GenreType third;

    public UserGenre(OauthMember oauthMember) {
        this.oauthMember = oauthMember;
        for (GenreType genre : GenreType.values()) {
            genreWeights.add(new UserGenreWeight(genre, 0, this));
        }
    }

    public void incrementGenreWeight(GenreType genreType) {
        genreWeights.stream()
                .filter(gw -> gw.getGenreType() == genreType)
                .findAny()
                .ifPresent(UserGenreWeight::incrementWeight);
        updateFavoriteGenre();
    }

    private void updateFavoriteGenre() {
        genreWeights.sort(Comparator.comparing(UserGenreWeight::getWeight).reversed());
        if (genreWeights.size() >= 3) {
            favorite = genreWeights.get(0).getGenreType();
            second = genreWeights.get(1).getGenreType();
            third = genreWeights.get(2).getGenreType();
        }
    }

    public GenreType favorite() {
        return favorite;
    }

    public GenreType second() {
        return second;
    }

    public GenreType third() {
        return third;
    }
}
