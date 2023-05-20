package ru.rofleksey.music_library.dao;

import java.util.List;

public record AlbumFullDao(
        Long id,
        String title,
        String thumbLink,
        Long rating,
        List<SongDao> songs
) {
}
