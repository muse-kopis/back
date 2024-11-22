package muse_kopis.muse.performance.domain.genre.application;

import muse_kopis.muse.performance.domain.genre.domain.GenreType;

public interface GenreService {

    void saveGenre(String performanceName, GenreType genreType);
}
