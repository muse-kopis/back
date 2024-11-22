package muse_kopis.muse.ticketbook.domain.photo.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import muse_kopis.muse.ticketbook.domain.photo.application.PhotoService;
import muse_kopis.muse.ticketbook.domain.photo.application.PhotoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    @Operation(description = "사진 여러장을 form data로 보내면 됩니다.(List<MultipartFile>타입으로 되어있습니다.")
    public ResponseEntity<List<String>> uploadImage(@RequestBody List<MultipartFile> photos) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile image : photos) {
            urls.add(photoService.upload(image));
        }
        return ResponseEntity.ok().body(urls);
    }

    @DeleteMapping
    @Operation(description = "사진 여러장을 한번에 지울 수 있습니다.(List<String>타입으로 되어있습니다.")
    public ResponseEntity<Void> deleteImage(@RequestBody List<String> photos) {
        photos.forEach(photoService::deleteImage);
        return ResponseEntity.ok().build();
    }
}
