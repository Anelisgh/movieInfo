package com.example.movieInfo.repository;

import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.WatchedMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovies, Long> {
    Optional<WatchedMovies> findByUserAndMovie(User user, Movie movie);
    Optional<WatchedMovies> findByUserAndMovieId(User user, Long movieId);
    List<WatchedMovies> findByUser(User user);
    boolean existsByUserAndMovieIdAndIsWatchedIsTrue(User user, Long movieId);
    void deleteAllByUser(User user);
}

