package com.example.movieInfo.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class WatchlistRequest {
    private Long id;
    @NotBlank
    @Size(max=200)
    private String name;
    private Set<Long> movieIds = new HashSet<>(); ;

    public WatchlistRequest() {
    }

    public WatchlistRequest(Long id, String name, Set<Long> movieIds) {
        this.id = id;
        this.name = name;
        this.movieIds = movieIds;
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

    public Set<Long> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(Set<Long> movieIds) {
        this.movieIds = movieIds;
    }
}

