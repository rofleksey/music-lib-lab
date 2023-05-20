package ru.rofleksey.music_library.dao;

public record SongDao(
        Long id,
        String title,
        String audioLink,
        String thumbLink,
        Long rating,
        Long artistId,
        Long albumId
) {
}
