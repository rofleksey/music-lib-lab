package ru.rofleksey.music_library.dao;

public record AlbumDao(
        Long id,
        String title,
        String thumbLink,
        Long rating
) {
}
