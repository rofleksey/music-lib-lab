package ru.rofleksey.music_library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rofleksey.music_library.dao.PageResponse;
import ru.rofleksey.music_library.dao.SongDao;
import ru.rofleksey.music_library.db.repo.SongRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class SongController {
    private static final int PAGE_ITEMS_COUNT = 50;

    private final SongRepo songRepo;

    @GetMapping()
    public PageResponse<SongDao> getSongs(@RequestParam Integer page) {
        if (page == null) {
            page = 0;
        }

        var pageable = PageRequest.of(page, PAGE_ITEMS_COUNT);
        var songs = songRepo
                .findAllBy(pageable)
                .stream()
                .map((song) -> new SongDao(
                        song.getId(),
                        song.getTitle(),
                        "/static/" + song.getAudioFile(),
                        "/static/" + song.getThumb(),
                        song.getRating(),
                        song.getArtist().getId(),
                        song.getAlbum().getId()
                )).toList();
        var count = Math.ceil(((double) songRepo.count()) / PAGE_ITEMS_COUNT);

        return new PageResponse<>(songs, (long) count);
    }
}
