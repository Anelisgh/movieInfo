package com.example.movieInfo.dto;

import jakarta.validation.constraints.NotNull;

public class WatchedMovieRequest {
    private Long userId;
    @NotNull
    private Long movieId;

    public WatchedMovieRequest(Long userId, Long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}

