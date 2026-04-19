package harjoitustyo.songvault.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import harjoitustyo.songvault.model.Song;
import harjoitustyo.songvault.model.SongRepository;
import harjoitustyo.songvault.model.User;
import harjoitustyo.songvault.model.UserRepository;

import java.security.Principal;
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
    private final UserRepository userRepository;

    public SongRestController(SongRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
    public Optional<Song> findSongRest(@PathVariable Long id){
        return repository.findById(id);
    }

    @PostMapping("/songs")
    public Song newSong(@RequestBody Song newSong, Principal principal) {

        if (principal == null) return null;

        User user = userRepository.findByUsername(principal.getName());
        newSong.setUser(user);

        return repository.save(newSong);
    }

    @PutMapping("/songs/{id}")
    public Song editSong(@PathVariable Long id, @RequestBody Song editedSong, Principal principal) {

        if (principal == null) return null;

        Song existing = repository.findById(id).orElse(null);
        if (existing == null || existing.getUser() == null) return null;

        User user = userRepository.findByUsername(principal.getName());
        boolean isAdmin = user.getRole().equals("ROLE_ADMIN");

        if (isAdmin || existing.getUser().getUsername().equals(principal.getName())) {

            editedSong.setId(id);
            editedSong.setUser(existing.getUser()); // säilytetään omistaja

            return repository.save(editedSong);
        }

        return null;
    }

    @DeleteMapping("/songs/{id}")
    public void deleteSong(@PathVariable Long id, Principal principal) {

        if (principal == null) return;

        Song song = repository.findById(id).orElse(null);
        if (song == null || song.getUser() == null) return;

        User user = userRepository.findByUsername(principal.getName());
        boolean isAdmin = user.getRole().equals("ROLE_ADMIN");

        if (isAdmin ||
            song.getUser().getUsername().equals(principal.getName())) {

            repository.delete(song);
        }
    }
}
