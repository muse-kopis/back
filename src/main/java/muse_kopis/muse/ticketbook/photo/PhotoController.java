package muse_kopis.muse.ticketbook.photo;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<List<String>> uploadImage(@RequestBody List<MultipartFile> images) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile image : images) {
            urls.add(photoService.upload(image));
        }
        return ResponseEntity.ok().body(urls);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestBody List<String> images) {
        images.forEach(photoService::deleteImageFromS3);
        return ResponseEntity.ok().build();
    }
}
