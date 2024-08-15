package muse_kopis.muse.review.dto;

public record ReviewRequest(
        String performanceName,
        String venue,
        String content,
        Integer star
) {
}
