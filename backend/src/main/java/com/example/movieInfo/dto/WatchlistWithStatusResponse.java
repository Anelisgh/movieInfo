package com.example.movieInfo.dto;

public class WatchlistWithStatusResponse {
    private Long id;
    private String name;
    private boolean containsMovie;

    public WatchlistWithStatusResponse(){}

    public WatchlistWithStatusResponse(Long id, String name, boolean containsMovie) {
        this.id = id;
        this.name = name;
        this.containsMovie = containsMovie;
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

    public boolean isContainsMovie() {
        return containsMovie;
    }

    public void setContainsMovie(boolean containsMovie) {
        this.containsMovie = containsMovie;
    }
}
