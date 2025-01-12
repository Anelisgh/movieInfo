package com.example.movieInfo.dto;

import jakarta.validation.constraints.*;

public class ReviewRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long movieId;
    @Min(1)
    @Max(10)
    private int rating;
    @Size(max=5000)
    private String comment;

    public ReviewRequest(Long userId, Long movieId, int rating, String comment) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
    }

    public ReviewRequest() {

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

