package com.example.movieInfo.dto;

import com.example.movieInfo.model.Genre;
import jakarta.validation.constraints.*;

import java.util.Set;

public class MovieRequest {
    @NotBlank
    @Size(max=250)
    private String title;
    @Size(max = 5000)
    private String description;
    @NotNull
    @Min(1880)
    @Max(2030)
    private int releaseYear;
    @NotNull
    private Genre genre;
    @NotBlank
    @Size(max = 100)
    private String director;
    @NotNull
    private Set<String> actors;
    @NotBlank
    private String photoUrl;

    public MovieRequest(){}

    public MovieRequest(String title, String description, int releaseYear, Genre genre, String director, Set<String> actors, String photoUrl) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.director = director;
        this.actors = actors;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public @NotNull Set<String> getActors() {
        return actors;
    }

    public void setActors(@NotNull Set<String> actors) {
        this.actors = actors;
    }

    public @NotBlank String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(@NotBlank String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

