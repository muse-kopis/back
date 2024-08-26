package muse_kopis.muse.ticketbook;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.ticketbook.dto.MonthDto;
import muse_kopis.muse.ticketbook.dto.TicketBookRequest;
import muse_kopis.muse.ticketbook.dto.TicketBookResponse;
import muse_kopis.muse.ticketbook.photo.PhotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ticketBooks")
public class TicketBookController {

    private final TicketBookService ticketBookService;
    private final PhotoService photoService;

    @GetMapping
    public ResponseEntity<List<TicketBookResponse>> ticketBooks(@Auth Long memberId) {
        return ResponseEntity.ok().body(ticketBookService.ticketBooks(memberId));
    }

    @GetMapping("/{ticketBookId}")
    public ResponseEntity<TicketBookResponse> ticketBook(@Auth Long memberId, @PathVariable Long ticketBookId) {
        return ResponseEntity.ok().body(ticketBookService.ticketBook(memberId, ticketBookId));
    }

    @GetMapping("/date")
    public ResponseEntity<List<TicketBookResponse>> ticketBookInDate(@Auth Long memberId, @RequestParam LocalDate localDate) {
        return ResponseEntity.ok().body(ticketBookService.ticketBookInDate(memberId, localDate));
    }

    @GetMapping("/month")
    public ResponseEntity<List<TicketBookResponse>> ticketBooksByMonth(@Auth Long memberId, @ModelAttribute MonthDto monthDto) {
        return ResponseEntity.ok().body(ticketBookService.ticketBooksForMonth(memberId, monthDto.year(), monthDto.month()));
    }

    @Operation(summary = "TicketBookRequest 스키마 참조",
            description = "{\"performanceId\":Long,\n viewDate:\"2024-08-23\",\n "
                    + "review : { \"content\": \"string\", \"star\": \"int\", \"castMembers\": [{\"name\":\"string\"}], visible:\"boolean\"}}")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> writeTicketBooks(@Auth Long memberId, @ModelAttribute TicketBookRequest ticketBookRequest) {
        List<String> urls = new ArrayList<>();
        if (ticketBookRequest.photos() != null) {
            urls = ticketBookRequest.photos().stream().map(photoService::upload).toList();
        }
        return ResponseEntity.ok()
                .body(
                        ticketBookService.writeTicketBook(
                                memberId,
                                ticketBookRequest.performanceId(),
                                ticketBookRequest.viewDate(),
                                urls,
                                ticketBookRequest.star(),
                                ticketBookRequest.content(),
                                ticketBookRequest.visible(),
                                ticketBookRequest.castMembers()
                        )
                );
    }

    @DeleteMapping("/{ticketBookId}")
    public ResponseEntity<Long> deleteTicketBooks(@Auth Long memberId, @PathVariable Long ticketBookId) {
        return ResponseEntity.ok().body(ticketBookService.deleteTicketBook(memberId, ticketBookId));
    }

    @PatchMapping("/{ticketBookId}")
    public ResponseEntity<Long> updateTicketBook(@Auth Long memberId, @PathVariable Long ticketBookId, @ModelAttribute TicketBookRequest ticketBookRequest) {
        List<String> urls = photoService.updateTicketBookImage(ticketBookId, ticketBookRequest.photos());
        return ResponseEntity.ok()
                .body(
                        ticketBookService.updateTicketBook(
                                memberId,
                                ticketBookId,
                                ticketBookRequest.viewDate(),
                                urls,
                                ticketBookRequest.star(),
                                ticketBookRequest.content(),
                                ticketBookRequest.visible(),
                                ticketBookRequest.castMembers()
                        )
                );
    }
}
