package ru.rofleksey.music_library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.rofleksey.music_library.dao.AlbumDao;
import ru.rofleksey.music_library.dao.AlbumFullDao;
import ru.rofleksey.music_library.dao.PageResponse;
import ru.rofleksey.music_library.dao.SongDao;
import ru.rofleksey.music_library.db.repo.AlbumRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
public class AlbumController {
    private static final int PAGE_ITEMS_COUNT = 50;

    private final AlbumRepo albumRepo;

    @GetMapping()
    public PageResponse<AlbumDao> getAlbums(@RequestParam Integer page) {
        if (page == null) {
            page = 0;
        }

        var pageable = PageRequest.of(page, PAGE_ITEMS_COUNT);
        var albums = albumRepo
                .findAllBy(pageable)
                .stream()
                .map((album) -> new AlbumDao(
                        album.getId(),
                        album.getTitle(),
                        "/static/" + album.getThumb(),
                        album.getRating()
                )).toList();
        var count = Math.ceil(((double) albumRepo.count()) / PAGE_ITEMS_COUNT);

        return new PageResponse<>(albums, (long) count);
    }

    @GetMapping("/{id}")
    public AlbumFullDao getAlbum(@PathVariable Long id) {
        var albumOpt = albumRepo.findById(id);
        if (albumOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }

        var album = albumOpt.get();

        return new AlbumFullDao(
                album.getId(),
                album.getTitle(),
                "/static/" + album.getThumb(),
                album.getRating(),
                album.getSongs().stream().map((song) -> new SongDao(
                        song.getId(),
                        song.getTitle(),
                        "/static/" + song.getAudioFile(),
                        "/static/" + song.getThumb(),
                        song.getRating(),
                        song.getArtist().getId(),
                        song.getAlbum().getId()
                )).toList()
        );
    }
}
