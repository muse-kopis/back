package muse_kopis.muse.performance.usergenre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.genre.GenreType;

@Entity
@NoArgsConstructor
public class UserGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private GenreType favorite;
    @OneToOne
    private OauthMember oauthMember;
    private Integer crime;
    private Integer fantasy;
    private Integer fairyTale;
    private Integer family;
    private Integer adventure;
    private Integer healing;
    private Integer romance;
    private Integer child;
    private Integer thriller;
    private Integer history;
    private Integer drama;
    private Integer philosophy;
    private Integer jazz;
    private Integer humor;
    private Integer comedy;
    private Integer rock;
    private Integer action;
    private Integer office;
    private Integer parody;
    private Integer nonverbal;
    private Integer performance;
    private Integer art;
    private Integer interactive;
    private Integer emotion;
    private Integer youthfulGrowth;
    private Integer cabaret;

    public UserGenre(OauthMember oauthMember) {
        this.oauthMember = oauthMember;
        this.crime = 0;
        this.fantasy = 0;
        this.fairyTale = 0;
        this.family = 0;
        this.adventure = 0;
        this.healing = 0;
        this.romance = 0;
        this.child = 0;
        this.thriller = 0;
        this.history = 0;
        this.drama = 0;
        this.philosophy = 0;
        this.jazz = 0;
        this.humor = 0;
        this.comedy = 0;
        this.rock = 0;
        this.action = 0;
        this.office = 0;
        this.parody = 0;
        this.nonverbal = 0;
        this.performance = 0;
        this.art = 0;
        this.interactive = 0;
        this.emotion = 0;
        this.youthfulGrowth = 0;
        this.cabaret = 0;
    }

    public void incrementGenreWeight(GenreType genreType) {
        switch (genreType) {
            case CRIME -> crime++;
            case FANTASY -> fantasy++;
            case FAIRY_TALE -> fairyTale++;
            case FAMILY -> family++;
            case ADVENTURE -> adventure++;
            case HEALING -> healing++;
            case ROMANCE -> romance++;
            case CHILD -> child++;
            case THRILLER -> thriller++;
            case HISTORY -> history++;
            case DRAMA -> drama++;
            case PHILOSOPHY -> philosophy++;
            case JAZZ -> jazz++;
            case HUMOR -> humor++;
            case COMEDY -> comedy++;
            case ROCK -> rock++;
            case ACTION -> action++;
            case OFFICE -> office++;
            case PARODY -> parody++;
            case NON_VERBAL -> nonverbal++;
            case PERFORMANCE -> performance++;
            case ART -> art++;
            case INTERACTIVE -> interactive++;
            case EMOTION -> emotion++;
            case YOUTHFUL_GROWTH -> youthfulGrowth++;
            case CABARET -> cabaret++;
        }
        updateFavoriteGenre();
    }

    private void updateFavoriteGenre() {
        Map<GenreType, Integer> genreWeights = new HashMap<>();
        genreWeights.put(GenreType.CRIME, crime);
        genreWeights.put(GenreType.FANTASY, fantasy);
        genreWeights.put(GenreType.FAIRY_TALE, fairyTale);
        genreWeights.put(GenreType.FAMILY, family);
        genreWeights.put(GenreType.ADVENTURE, adventure);
        genreWeights.put(GenreType.HEALING, healing);
        genreWeights.put(GenreType.ROMANCE, romance);
        genreWeights.put(GenreType.CHILD, child);
        genreWeights.put(GenreType.THRILLER, thriller);
        genreWeights.put(GenreType.HISTORY, history);
        genreWeights.put(GenreType.DRAMA, drama);
        genreWeights.put(GenreType.PHILOSOPHY, philosophy);
        genreWeights.put(GenreType.JAZZ, jazz);
        genreWeights.put(GenreType.HUMOR, humor);
        genreWeights.put(GenreType.COMEDY, comedy);
        genreWeights.put(GenreType.ROCK, rock);
        genreWeights.put(GenreType.ACTION, action);
        genreWeights.put(GenreType.OFFICE, office);
        genreWeights.put(GenreType.PARODY, parody);
        genreWeights.put(GenreType.NON_VERBAL, nonverbal);
        genreWeights.put(GenreType.PERFORMANCE, performance);
        genreWeights.put(GenreType.ART, art);
        genreWeights.put(GenreType.INTERACTIVE, interactive);
        genreWeights.put(GenreType.EMOTION, emotion);
        genreWeights.put(GenreType.YOUTHFUL_GROWTH, youthfulGrowth);
        genreWeights.put(GenreType.CABARET, cabaret);

        favorite = Collections.max(genreWeights.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
