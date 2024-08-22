package muse_kopis.muse.ticketbook;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.ticketbook.dto.MonthDto;
import muse_kopis.muse.ticketbook.dto.TicketBookRequest;
import muse_kopis.muse.ticketbook.dto.TicketBookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ticketBooks")
public class TicketBookController {

    private final TicketBookService ticketBookService;

    @GetMapping
    public ResponseEntity<List<TicketBookResponse>> ticketBooks(@Auth Long memberId) {
        return ResponseEntity.ok().body(ticketBookService.ticketBooks(memberId));
    }

    @GetMapping("/{ticketBookId}")
    public ResponseEntity<TicketBookResponse> ticketBook(@Auth Long memberId, @PathVariable Long ticketBookId) {
        return ResponseEntity.ok().body(ticketBookService.ticketBook(memberId, ticketBookId));
    }

    @GetMapping("/date")
    public ResponseEntity<TicketBookResponse> ticketBookInDate(@Auth Long memberId, @RequestParam LocalDate localDate) {
        return ResponseEntity.ok().body(ticketBookService.ticketBookInDate(memberId, localDate));
    }

    @GetMapping("/month")
    public ResponseEntity<List<TicketBookResponse>> ticketBooksByMonth(@Auth Long memberId, @ModelAttribute MonthDto monthDto) {
        log.error("year {}",monthDto.year());
        return ResponseEntity.ok().body(ticketBookService.ticketBooksForMonth(memberId, monthDto.year(), monthDto.month()));
    }

    @PostMapping
    public ResponseEntity<Long> writeTicketBooks(@Auth Long memberId, @RequestBody TicketBookRequest ticketBookRequest) {
        return ResponseEntity.ok()
                .body(
                        ticketBookService.writeTicketBook(
                        memberId,
                        ticketBookRequest.performanceId(),
                        ticketBookRequest.viewDate(),
                        ticketBookRequest.photos(),
                        ticketBookRequest.review())
                );
    }

    @DeleteMapping("/{ticketBookId}")
    public ResponseEntity<Long> deleteTicketBooks(@Auth Long memberId, @PathVariable Long ticketBookId) {
        return ResponseEntity.ok().body(ticketBookService.deleteTicketBook(memberId, ticketBookId));
    }
}
