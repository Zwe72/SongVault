package harjoitustyo.songvault.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long>{

    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrGenreContainingIgnoreCase(
        String title, String artist, String genre
    );
    
    List<Song> findByUser(User user);
}
