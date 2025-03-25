package com.example.movieInfo.dto;

import java.util.Set;

public class WatchlistResponse {
    private Long id;
    private String name;
    private Set<MovieDetails> movies;

    public WatchlistResponse() {}

    public WatchlistResponse(Long id, String name, Set<MovieDetails> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MovieDetails> getMovies() {
        return movies;
    }

    public void setMovies(Set<MovieDetails> movies) {
        this.movies = movies;
    }
}
