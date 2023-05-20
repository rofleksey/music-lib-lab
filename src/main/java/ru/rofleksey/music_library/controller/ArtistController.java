package ru.rofleksey.music_library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.rofleksey.music_library.dao.*;
import ru.rofleksey.music_library.db.repo.ArtistRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/artist")
public class ArtistController {
    private static final int PAGE_ITEMS_COUNT = 50;

    private final ArtistRepo artistRepo;

    @GetMapping()
    public PageResponse<ArtistDao> getArtists(@RequestParam Integer page) {
        if (page == null) {
            page = 0;
        }

        var pageable = PageRequest.of(page, PAGE_ITEMS_COUNT);
        var artists = artistRepo
                .findAllBy(pageable)
                .stream()
                .map((artist) -> new ArtistDao(
                        artist.getId(),
                        artist.getName(),
                        "/static/" + artist.getThumb()
                )).toList();
        var count = Math.ceil(((double) artistRepo.count()) / PAGE_ITEMS_COUNT);

        return new PageResponse<>(artists, (long) count);
    }

    @GetMapping("/{id}")
    public ArtistFullDao getArtist(@PathVariable Long id) {
        var artistOpt = artistRepo.findById(id);
        if (artistOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }

        var artist = artistOpt.get();

        return new ArtistFullDao(
                artist.getId(),
                artist.getName(),
                "/static/" + artist.getThumb(),
                artist.getAlbums().stream().map((album) -> new AlbumDao(
                        album.getId(),
                        album.getTitle(),
                        "/static/" + album.getThumb(),
                        album.getRating()
                )).toList(),
                artist.getSongs().stream().map((song) -> new SongDao(
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
