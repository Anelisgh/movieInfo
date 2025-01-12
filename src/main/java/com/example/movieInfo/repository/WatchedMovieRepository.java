package com.example.movieInfo.repository;

import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.WatchedMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovies, Long> {
    Optional<WatchedMovies> findByUserAndMovie(User user, Movie movie);
}

