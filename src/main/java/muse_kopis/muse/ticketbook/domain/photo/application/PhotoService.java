package muse_kopis.muse.ticketbook.domain.photo.application;

import muse_kopis.muse.ticketbook.domain.TicketBook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    String upload(MultipartFile image);
    void deleteImage(String imageAddress);
    void updateImage(TicketBook ticketBook, List<String> urls);
}
