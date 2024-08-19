package muse_kopis.muse.ticketbook;

import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.ticketbook.dto.TicketBookRequest;
import muse_kopis.muse.ticketbook.dto.TicketBookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticketBooks")
public class TicketBookController {

    private final TicketBookService ticketBookService;

    @GetMapping
    public List<TicketBookResponse> ticketBooks(@Auth Long memberId) {
        return ticketBookService.ticketBooks(memberId);
    }

    @GetMapping("/{ticketBookId}")
    public TicketBookResponse ticketBook(@Auth Long memberId, @PathVariable Long ticketBookId) {
        return ticketBookService.ticketBook(memberId, ticketBookId);
    }

    @PostMapping
    public ResponseEntity<Long> writeTicketBooks(@Auth Long memberId, @RequestBody TicketBookRequest ticketBookRequest) {
        return ResponseEntity.ok()
                .body(ticketBookService.writeTicketBook(
                        memberId,
                        ticketBookRequest.performanceId(),
                        ticketBookRequest.viewDate(),
                        ticketBookRequest.photos(),
                        ticketBookRequest.review()));
    }

    @DeleteMapping("/{ticketBookId}")
    public ResponseEntity<Long> deleteTicketBooks(@Auth Long memberId, @PathVariable Long ticketBookId) {
        return ResponseEntity.ok().body(ticketBookService.deleteTicketBook(memberId, ticketBookId));
    }
}
