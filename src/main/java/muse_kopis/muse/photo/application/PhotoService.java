package muse_kopis.muse.photo.application;

import muse_kopis.muse.photo.domain.Photo;
import muse_kopis.muse.ticketbook.domain.TicketBook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    String upload(MultipartFile image);
    void deleteImage(String imageAddress);
    void updateImage(TicketBook ticketBook, List<String> urls);
    void deleteImages(TicketBook ticketBook);
    List<Photo> getImagesByTicketBook(TicketBook ticketBook);
}
