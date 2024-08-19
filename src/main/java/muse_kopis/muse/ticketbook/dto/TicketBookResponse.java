package muse_kopis.muse.ticketbook.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

import muse_kopis.muse.review.dto.ReviewResponse;
import muse_kopis.muse.ticketbook.TicketBook;

@Builder
public record TicketBookResponse(
        Long id,
        LocalDate viewDate,
        String venue,
        List<PhotoDto> photos,
        ReviewResponse reviewResponse
) {
    public static TicketBookResponse from(TicketBook ticketBook) {
        return TicketBookResponse.builder()
                .id(ticketBook.getId())
                .viewDate(ticketBook.getViewDate())
                .venue(ticketBook.getVenue())
                .photos(ticketBook.getPhotos().stream()
                        .map(photo -> new PhotoDto(photo.getUrl()))
                        .collect(Collectors.toList()))
                .reviewResponse(ReviewResponse.from(ticketBook.getReview()))
                .build();
    }
}
