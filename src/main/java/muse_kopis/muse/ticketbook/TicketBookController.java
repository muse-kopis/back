package muse_kopis.muse.ticketbook;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.ticketbook.config.FrontURLConfig;
import muse_kopis.muse.ticketbook.dto.MonthDto;
import muse_kopis.muse.ticketbook.dto.ShareablePage;
import muse_kopis.muse.ticketbook.dto.TicketBookCalender;
import muse_kopis.muse.ticketbook.dto.TicketBookEditRequest;
import muse_kopis.muse.ticketbook.dto.TicketBookRequest;
import muse_kopis.muse.ticketbook.dto.TicketBookResponse;
import muse_kopis.muse.ticketbook.photo.PhotoService;
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

    private final FrontURLConfig frontURLConfig;
    private final TicketBookService ticketBookService;
    private final PhotoService photoService;

    @GetMapping("/share/{identifier}")
    public ResponseEntity<ShareablePage> getSharedTicketBooks(@PathVariable String identifier) {
        return ResponseEntity.ok().body(ticketBookService.findByIdentifier(identifier));
    }

    @GetMapping("/share/detail/{identifier}")
    @Operation(summary = "티켓북 공유",
            description = "url을 가지고 공유한 티켓북의 상세 내용을 조회합니다.")
    public ResponseEntity<TicketBookResponse> getSharedTicketBook(@PathVariable String identifier) {
        return ResponseEntity.ok().body(ticketBookService.sharedTicketBook(identifier));
    }

    @Operation(summary = "티켓북 공유",
            description = "memberId를 이용하여 공유할 티켓북 페이지 url을 만듭니다.")
    @PostMapping("/share")
    public ResponseEntity<String> generateSharedTicketBookLink(@Auth Long memberId) {
        return ResponseEntity.ok().body(frontURLConfig.url()+"ticketBooks/share/"+ticketBookService.generateLink(memberId));
    }

    @GetMapping
    @Operation(summary = "티켓북 조회",
            description = "사용자가 저장한 티켓북을 조회합니다.")
    public ResponseEntity<List<TicketBookResponse>> ticketBooks(@Auth Long memberId) {
        return ResponseEntity.ok().body(ticketBookService.ticketBooks(memberId));
    }

    @GetMapping("/{ticketBookId}")
    public ResponseEntity<TicketBookResponse> ticketBook(@PathVariable Long ticketBookId) {
        return ResponseEntity.ok().body(ticketBookService.ticketBook(ticketBookId));
    }

    @GetMapping("/date")
    @Operation(summary = "포토캘린더 특정 날짜 조회",
            description = "해당일에 기록해둔 티켓북들을 모두 조회합니다.")
    public ResponseEntity<List<TicketBookResponse>> ticketBookInDate(@Auth Long memberId, @RequestParam LocalDate localDate) {
        return ResponseEntity.ok().body(ticketBookService.ticketBookInDate(memberId, localDate));
    }

    @GetMapping("/month")
    @Operation(summary = "포토캘린더 조회",
            description = "등록한 티켓북을 달별로 조회합니다.")
    public ResponseEntity<Map<LocalDate, List<TicketBookCalender>>> ticketBooksByMonth(@Auth Long memberId, @ModelAttribute MonthDto monthDto) {
        return ResponseEntity.ok().body(ticketBookService.ticketBooksForMonth(memberId, monthDto.year(), monthDto.month()));
    }

    @Operation(summary = "티켓북 등록",
            description = "photos는 url을 보내면 됩니다.(List<String>타입으로 되어있습니다.)")
    @PostMapping
    public ResponseEntity<Long> writeTicketBooks(@Auth Long memberId, @ModelAttribute TicketBookRequest ticketBookRequest) {
        return ResponseEntity.ok()
                .body(
                        ticketBookService.writeTicketBook(
                                memberId,
                                ticketBookRequest.performanceId(),
                                ticketBookRequest.viewDate(),
                                ticketBookRequest.photos(),
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
    @Operation(description = "photos는 url을 보내면 됩니다.(List<String>타입으로 되어있습니다.)")
    public ResponseEntity<Long> updateTicketBook(@Auth Long memberId, @PathVariable Long ticketBookId, @ModelAttribute TicketBookEditRequest ticketBookEditRequest) {
        return ResponseEntity.ok()
                .body(
                        ticketBookService.updateTicketBook(
                                memberId,
                                ticketBookId,
                                ticketBookEditRequest.viewDate(),
                                ticketBookEditRequest.star(),
                                ticketBookEditRequest.content(),
                                ticketBookEditRequest.visible(),
                                ticketBookEditRequest.castMembers(),
                                ticketBookEditRequest.photos()
                        )
                );
    }
}
