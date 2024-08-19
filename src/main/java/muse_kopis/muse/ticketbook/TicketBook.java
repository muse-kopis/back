package muse_kopis.muse.ticketbook;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.performance.Performance;
import muse_kopis.muse.performance.castmember.CastMember;
import muse_kopis.muse.review.Review;
import muse_kopis.muse.review.dto.ReviewResponse;
import muse_kopis.muse.ticketbook.photo.Photo;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate viewDate;
    private String venue;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<CastMember> castMembers;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    private OauthMember oauthMember;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;

    public static TicketBook from(
            OauthMember oauthMember,
            LocalDate viewDate,
            ReviewResponse review,
            Performance performance
    ) {
        return TicketBook.builder()
                .oauthMember(oauthMember)
                .viewDate(viewDate)
                .venue(performance.getVenue())
                .castMembers(review.castMembers().stream().map(cast -> new CastMember(cast.name(), performance))
                        .collect(Collectors.toList()))
                .review(Review.builder()
                        .star(review.star())
                        .content(review.content())
                        .visible(review.visible())
                        .performance(performance)
                        .build())
                .build();
    }
}
