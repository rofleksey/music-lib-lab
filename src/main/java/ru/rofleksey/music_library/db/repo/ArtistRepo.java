package ru.rofleksey.music_library.db.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rofleksey.music_library.db.model.Artist;

import java.util.List;

@Repository()
public interface ArtistRepo extends CrudRepository<Artist, Long> {
    List<Artist> findAllBy(Pageable pageable);
}
