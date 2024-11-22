package muse_kopis.muse.ticketbook.domain.photo.infra;

import java.util.Arrays;
import java.util.List;
import muse_kopis.muse.common.s3.S3Exception;

public class PhotoValidator {

    public void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception("이미지의 확장자가 없습니다.");
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3Exception("부적절한 확장자 입니다.");
        }
    }
}
