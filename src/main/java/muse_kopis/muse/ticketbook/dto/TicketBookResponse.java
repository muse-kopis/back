package muse_kopis.muse.ticketbook.dto;

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
        LocalDateTime viewDate,
        String venue,
        List<PhotoResponse> photos,
        ReviewResponse reviewResponse
) {
    public static TicketBookResponse from(TicketBook ticketBook, List<Photo> photos) {
        return TicketBookResponse.builder()
                .id(ticketBook.getId())
                .viewDate(ticketBook.getViewDate())
                .venue(ticketBook.getVenue())
                .photos(photos.stream()
                        .map(photo -> new PhotoResponse(photo.getUrl()))
                        .collect(Collectors.toList()))
                .reviewResponse(ReviewResponse.from(ticketBook.getReview()))
                .build();
    }
}
