package muse_kopis.muse.usergenre.domain.dto;

import lombok.Builder;
import muse_kopis.muse.genre.domain.GenreType;
import muse_kopis.muse.usergenre.domain.UserGenre;

@Builder
public record UserGenreResponse(
        GenreType favorite,
        GenreType second,
        GenreType third
) {
    public static UserGenreResponse from(UserGenre userGenre) {
        return UserGenreResponse.builder()
                .favorite(userGenre.favorite())
                .second(userGenre.second())
                .third(userGenre.third())
                .build();
    }
}
