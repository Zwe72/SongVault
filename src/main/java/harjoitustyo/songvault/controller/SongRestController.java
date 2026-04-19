package harjoitustyo.songvault.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import harjoitustyo.songvault.model.Song;
import harjoitustyo.songvault.model.SongRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/api")
public class SongRestController {
    private final SongRepository repository;

    public SongRestController(SongRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/songs")
public List<Song> songListRest(@RequestParam(required = false) String keyword) {
    if (keyword != null && !keyword.isEmpty()) {
        return repository
    .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrGenreContainingIgnoreCase(
        keyword, keyword, keyword);
    } else {
        return (List<Song>) repository.findAll();
    }
}

    @GetMapping("/songs/{id}")
    public Optional<Song> findSongRest(@PathVariable("id") Long songId){
        return repository.findById(songId);
    }

    @PostMapping("/songs")
    public Song newSong(@RequestBody Song newSong) {

        return repository.save(newSong);
    }

    @PutMapping("/songs/{id}")
    public Song editSong(@PathVariable Long id, @RequestBody Song editedSong) {
        editedSong.setId(id);
        return repository.save(editedSong);
    }

    @DeleteMapping("/songs/{id}")
    public void deleteSong(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
