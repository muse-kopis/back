package muse_kopis.muse.ticketbook.application;

import jakarta.transaction.Transactional;
import muse_kopis.muse.ticketbook.domain.dto.ShareablePage;
import muse_kopis.muse.ticketbook.domain.dto.TicketBookCalender;
import muse_kopis.muse.ticketbook.domain.dto.TicketBookResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TicketBookService {

    @Transactional
    List<TicketBookResponse> getTicketBooks(Long memberId);
    @Transactional
    TicketBookResponse getTicketBook(Long ticketBookId);
    @Transactional
    Long writeTicketBook(Long memberId, Long performanceId, LocalDateTime viewDate, List<String> urls, Integer star, String content, Boolean visible, String castMembers);
    @Transactional
    Long updateTicketBook(Long memberId, Long ticketBookId, LocalDateTime viewDate, List<String> urls, Integer star, String content, Boolean visible, String castMembers);
    @Transactional
    Long deleteTicketBook(Long memberId, Long ticketBookId);
    @Transactional
    List<TicketBookResponse> ticketBookInDate(Long memberId, LocalDate localDate);
    @Transactional
    Map<LocalDate, List<TicketBookCalender>> ticketBooksForMonth(Long memberId, Integer year, Integer month);
    @Transactional
    ShareablePage findByIdentifier(String identifier);
    @Transactional
    String generateLink(Long memberId);
    @Transactional
    TicketBookResponse getSharedTicketBook(String identifier);
}
