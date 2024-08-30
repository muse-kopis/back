package muse_kopis.muse.ticketbook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

import muse_kopis.muse.review.dto.ReviewResponse;
import muse_kopis.muse.ticketbook.TicketBook;
import muse_kopis.muse.ticketbook.photo.Photo;
import muse_kopis.muse.ticketbook.photo.dto.PhotoResponse;

@Builder
public record TicketBookResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd a hh:mm", locale = "ko_KR")
        LocalDateTime viewDate,
        String venue,
        List<PhotoResponse> photos,
        ReviewResponse reviewResponse,
        String performanceName,
        String poster,
        Long performanceId
) {
    public static TicketBookResponse from(TicketBook ticketBook, List<Photo> photos) {
        return TicketBookResponse.builder()
                .id(ticketBook.getId())
                .viewDate(ticketBook.getViewDate())
                .venue(ticketBook.getVenue())
                .photos(photos.stream()
                        .map(photo -> new PhotoResponse(photo.getUrl()))
                        .collect(Collectors.toList()))
                .reviewResponse(ReviewResponse.from(ticketBook.getReview(), ticketBook.getCastMembers()))
                .performanceName(ticketBook.getReview().getPerformance().getPerformanceName())
                .poster(ticketBook.getReview().getPerformance().getPoster())
                .performanceId(ticketBook.getReview().getPerformance().getId())
                .build();
    }
}
