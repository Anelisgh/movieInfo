package com.example.movieInfo.dto;

import com.example.movieInfo.model.ReviewType;
import jakarta.validation.constraints.*;

public class ReviewRequest {
    private Long userId;
    @NotNull
    private Long movieId;
    @Min(1)
    @Max(10)
    private int rating;
    @Size(max=5000)
    private String comment;
    @NotNull
    private ReviewType reviewType;

    public ReviewRequest(Long userId, Long movieId, int rating, String comment, ReviewType reviewType) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
        this.reviewType = reviewType;
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

    public @NotNull ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(@NotNull ReviewType reviewType) {
        this.reviewType = reviewType;
    }
}

