package com.example.movieInfo.dto;

public class MovieDetails {
    private Long id;
    private String title;
    private String photoUrl;
    private Integer releaseYear;

    public MovieDetails(Long id, String title, String photoUrl, Integer releaseYear) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
        this.releaseYear = releaseYear;
    }

    public MovieDetails(String title, String photoUrl, Integer releaseYear) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.releaseYear = releaseYear;
    }

    public MovieDetails() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
