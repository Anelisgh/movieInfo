package com.example.movieInfo.dto;

import java.util.List;
import java.util.Set;

public class MovieUserDetails {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String genre;
    private String directorName;
    private boolean isWatched;
    private Set<String> watchlists;
    private List<ReviewResponse> reviews;
    private double averageRating;
    private Set<String> actors;
    private String photoUrl;

    public MovieUserDetails(Long id, String title, String description, Integer releaseYear, String genre, String directorName, boolean isWatched, Set<String> watchlists, List<ReviewResponse> reviews, double averageRating, Set<String> actors, String photoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.directorName = directorName;
        this.isWatched = isWatched;
        this.watchlists = watchlists;
        this.reviews = reviews;
        this.averageRating = averageRating;
        this.actors = actors;
        this.photoUrl = photoUrl;
    }

    public MovieUserDetails() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public Set<String> getWatchlists() {
        return watchlists;
    }

    public void setWatchlists(Set<String> watchlists) {
        this.watchlists = watchlists;
    }

    public Set<String> getActors() {
        return actors;
    }

    public void setActors(Set<String> actors) {
        this.actors = actors;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}

