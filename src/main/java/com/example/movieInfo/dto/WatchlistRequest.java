package com.example.movieInfo.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class WatchlistRequest {
    private Long id;
    @NotBlank
    @Size(max=200)
    private String name;
    @NotNull
    private Long userId;
    private Set<Long> movieIds;

    public WatchlistRequest() {
    }

    public WatchlistRequest(Long id, String name, Long userId, Set<Long> movieIds) {
        this.id = id;
        this.name = name;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(Set<Long> movieIds) {
        this.movieIds = movieIds;
    }
}

