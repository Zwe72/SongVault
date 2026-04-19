package harjoitustyo.songvault.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import harjoitustyo.songvault.model.Song;
import harjoitustyo.songvault.model.SongRepository;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SongController {
    private final SongRepository repository;

    public SongController(SongRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/songs")
    public @ResponseBody List <Song> allSongs() {
        return (List<Song>) repository.findAll();
    }

    @RequestMapping("/songs/{id}")
    public @ResponseBody Optional<Song> findSong(@PathVariable("id") Long songId) {
        
        return repository.findById(songId);
    }

    @JsonIgnoreProperties("songs")
    @ManyToOne
    @JoinColumn()

    @GetMapping("/songlist")
    public String songList(Model model) {
        model.addAttribute("songs", repository.findAll());
        return "songlist";
    }

    @GetMapping("/addsong")
    public String addSong(Model model) {
        model.addAttribute("song", new Song());
        return "addsong";
    }

    @PostMapping("/savesong")
    public String save(Song song) {
        repository.save(song);
        return "redirect:/songlist";
    }

    @GetMapping("/deletesong/{id}")
    public String deleteSong(@PathVariable() Long id) {
        repository.deleteById(id);
        return "redirect:/songlist";
    }

    @GetMapping("/editsong/{id}")
    public String editSong(@PathVariable() Long id, Model model) {
        Song song = repository.findById(id).orElse(null);
        model.addAttribute("song", song);
        return "addsong";
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
