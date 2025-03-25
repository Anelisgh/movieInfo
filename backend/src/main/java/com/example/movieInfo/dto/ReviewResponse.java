package com.example.movieInfo.dto;

import com.example.movieInfo.model.ReviewType;

public class ReviewResponse {
    private Long id;
    private Double rating;
    private String comment;
    private Long userId;
    private String username;
    private Long movieId;
    private ReviewType reviewType;

    public ReviewResponse(){

    }

    public ReviewResponse(Long id, Double rating, String comment, Long userId, String username, Long movieId, ReviewType reviewType) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.username = username;
        this.movieId = movieId;
        this.reviewType = reviewType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

