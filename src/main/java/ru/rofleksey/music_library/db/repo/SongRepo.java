package ru.rofleksey.music_library.db.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rofleksey.music_library.db.model.Song;

import java.util.List;

@Repository()
public interface SongRepo extends CrudRepository<Song, Long> {
    List<Song> findAllBy(Pageable pageable);
}
