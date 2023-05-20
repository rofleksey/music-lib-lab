package ru.rofleksey.music_library.db.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rofleksey.music_library.db.model.Album;

import java.util.List;

@Repository()
public interface AlbumRepo extends CrudRepository<Album, Long> {
    List<Album> findAllBy(Pageable pageable);
}
