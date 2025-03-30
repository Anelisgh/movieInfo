package com.example.movieInfo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "watched_movies")
public class WatchedMovies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "is_watched")
    private Boolean isWatched;

    @Column(name = "watch_date")
    private LocalDate watchDate;

    @PrePersist
    @PreUpdate
    public void setDefaultValues() {
        if (isWatched && watchDate == null) {
            watchDate = LocalDate.now();
        }
    }

    public WatchedMovies() {
        this.isWatched = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setIsWatched(boolean watched) {
        isWatched = watched;
    }

    public LocalDate getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(LocalDate watchDate) {
        this.watchDate = watchDate;
    }
}


