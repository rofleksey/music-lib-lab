package ru.rofleksey.music_library.dao;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        Long pageCount
) {
}
