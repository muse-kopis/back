package muse_kopis.muse.review.domain.dto;

public record ReviewRequest(
        String performanceName,
        String venue,
        String content,
        Integer star,
        Boolean visible
) {
}
