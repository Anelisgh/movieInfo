package com.example.movieInfo.dto;

import java.util.Set;

public class WatchlistResponse {
    private String name;
    private Set<String> movieTitles;

    public WatchlistResponse() {}

    public WatchlistResponse(String name, Set<String> movieTitles) {
        this.name = name;
        this.movieTitles = movieTitles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMovieTitles() {
        return movieTitles;
    }

    public void setMovieTitles(Set<String> movieTitles) {
        this.movieTitles = movieTitles;
    }
}
