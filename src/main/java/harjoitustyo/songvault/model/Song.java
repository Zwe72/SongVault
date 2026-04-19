package harjoitustyo.songvault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Artist is required")
    private String artist;
    
    private String genre;
    private int year;
    private LocalDate addedDate;


    public Song() {}

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
    return user;
    }

    public void setUser(User user) {
    this.user = user;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getArtist() {
        return artist;
    }


    public void setArtist(String artist) {
        this.artist = artist;
    }


    public String getGenre() {
        return genre;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }


    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public String toString() {
        return 
        "Song [id=" + id + 
        ", title=" + title + 
        ", artist=" + artist + 
        ", genre=" + genre + 
        ", year=" + year + 
        "]";
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }


}
