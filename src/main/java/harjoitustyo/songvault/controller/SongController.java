package harjoitustyo.songvault.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import harjoitustyo.songvault.model.Playlist;
import harjoitustyo.songvault.model.PlaylistRepository;
import harjoitustyo.songvault.model.Song;
import harjoitustyo.songvault.model.SongRepository;
import harjoitustyo.songvault.model.User;
import harjoitustyo.songvault.model.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SongController {
    private final SongRepository repository;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;

    public SongController(SongRepository repository, 
                        UserRepository userRepository,
                        PlaylistRepository playlistRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
    }

    @RequestMapping("/songs")
    public @ResponseBody List <Song> allSongs() {
        return (List<Song>) repository.findAll();
    }

    @RequestMapping("/songs/{id}")
    public @ResponseBody Optional<Song> findSong(@PathVariable("id") Long songId) {
        
        return repository.findById(songId);
    }

    @GetMapping("/addsong")
    public String addSong(Model model) {
        model.addAttribute("song", new Song());
        return "addsong";
    }

    @PostMapping("/savesong")
    public String save(Song song, Principal principal) {
    
        User user = userRepository.findByUsername(principal.getName());
        song.setUser(user);

        song.setAddedDate(LocalDate.now());

        repository.save(song);

        return "redirect:/songlist";
    }

    @PostMapping("/deletesong/{id}")
    public String deleteSong(@PathVariable Long id, Principal principal) {

    Song song = repository.findById(id).orElse(null);

    if (song != null && song.getUser() != null) {

        User user = userRepository.findByUsername(principal.getName());

        if (user.getRole().equals("ROLE_ADMIN") ||
            song.getUser().getUsername().equals(principal.getName())) {

            repository.delete(song);
        }
    }

    return "redirect:/songlist";
    }

    @GetMapping("/editsong/{id}")
    public String editSong(@PathVariable() Long id, Model model, Principal principal) {

        Song song = repository.findById(id).orElse(null);

        if (song != null && song.getUser() != null) {

    User user = userRepository.findByUsername(principal.getName());

    if (user.getRole().equals("ROLE_ADMIN") ||
        song.getUser().getUsername().equals(principal.getName())) {

        model.addAttribute("song", song);
        return "addsong";
    }
}

    return "redirect:/songlist";
    }

    @GetMapping("/songlist")
    public String songList(@RequestParam(required = false) String keyword, 
                        Model model,
                        Principal principal) {

    if (principal == null) {
        return "redirect:/login";
    }

    User user = userRepository.findByUsername(principal.getName());
    boolean isAdmin = user.getRole().equals("ROLE_ADMIN");

    if (isAdmin) {

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("songs", repository
                .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrGenreContainingIgnoreCase(
                    keyword, keyword, keyword));
        } else {
            model.addAttribute("songs", repository.findAll());
        }

    } else {

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("songs", repository
                .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrGenreContainingIgnoreCase(
                    keyword, keyword, keyword)
                .stream()
                .filter(song -> song.getUser() != null && song.getUser().equals(user))
                .toList());
        } else {
            model.addAttribute("songs", repository.findByUser(user));
        }
    }

    return "songlist";
    }

    @GetMapping("/playlists")
    public String playlists(Model model, Principal principal) {

    User user = userRepository.findByUsername(principal.getName());
    List<Playlist> playlists = playlistRepository.findByUser(user);

    model.addAttribute("playlists", playlists);

    return "playlists";
    }
    
    @GetMapping("/playlist/{id}")
    public String viewPlaylist(@PathVariable Long id, Model model, Principal principal) {

    Playlist playlist = playlistRepository.findById(id).orElse(null);

    if (playlist == null) {
        return "redirect:/songlist";
    }

    if (playlist.getUser() != null &&
    !playlist.getUser().getUsername().equals(principal.getName())) {
        return "redirect:/songlist";
    }

    model.addAttribute("playlist", playlist);
    return "playlist";
    }

    @PostMapping("/playlist/create")
    public String createPlaylist(@RequestParam String name, Principal principal) {

    User user = userRepository.findByUsername(principal.getName());

    Playlist playlist = new Playlist(name, user);
    playlistRepository.save(playlist);

    return "redirect:/playlists";
    }

    @PostMapping("/playlist/add/{playlistId}/{songId}")
    public String addSongToPlaylist(@PathVariable Long playlistId,
                               @PathVariable Long songId,
                               Principal principal) {

    Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
    Song song = repository.findById(songId).orElse(null);

    if (playlist != null && song != null) {

        if (playlist.getUser() != null &&
                playlist.getUser().getUsername().equals(principal.getName())) {

                playlist.getSongs().add(song);
                playlistRepository.save(playlist);
            }
        }

        return "redirect:/songlist";
    }

    @PostMapping("/playlist/remove/{playlistId}/{songId}")
    public String removeSongFromPlaylist(@PathVariable Long playlistId,
                                     @PathVariable Long songId,
                                     Principal principal) {

        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        Song song = repository.findById(songId).orElse(null);

        if (playlist != null && song != null) {

            if (playlist.getUser() != null &&
                playlist.getUser().getUsername().equals(principal.getName())) {

                playlist.getSongs().remove(song); // 🔥 tämä tekee poiston
                playlistRepository.save(playlist);
            }
        }

    return "redirect:/playlist/" + playlistId;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }
    
}
