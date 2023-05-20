package ru.rofleksey.music_library.dao;

import java.util.List;

public record ArtistFullDao(
        Long id,
        String name,
        String thumbLink,
        List<AlbumDao> albums,
        List<SongDao> songs
) {
}
