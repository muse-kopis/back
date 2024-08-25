package muse_kopis.muse.ticketbook.photo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.common.S3Exception;
import muse_kopis.muse.ticketbook.TicketBook;
import muse_kopis.muse.ticketbook.TicketBookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final static PhotoValidator validator = new PhotoValidator();

    private final TicketBookRepository ticketBookRepository;
    private final PhotoRepository photoRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Exception("빈 파일입니다.");
        }
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        validator.validateImageFileExtention(Objects.requireNonNull(image.getOriginalFilename()));
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new S3Exception("이미지 업로드에 실패했습니다.");
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;
        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentDisposition("inline");
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        }catch (Exception e){
            throw new S3Exception("s3에 이미지 입력을 실패했습니다.");
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new S3Exception("삭제에 실패했습니다.");
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            log.info(decodingKey);
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new S3Exception("삭제에 실패했습니다.");
        }
    }

    public List<String> updateTicketBookImage(Long ticketBookId, List<MultipartFile> photos) {
        TicketBook ticketBook = ticketBookRepository.getByTicketBookId(ticketBookId);
        photoRepository.findAllByTicketBook(ticketBook).forEach(photo -> deleteImageFromS3(photo.getUrl()));
        if (photos == null) {
            photos = new ArrayList<>();
        }
        return photos.stream().map(this::upload).toList();
    }
}
