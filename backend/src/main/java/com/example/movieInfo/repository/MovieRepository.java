package com.example.movieInfo.repository;

import com.example.movieInfo.model.Actor;
import com.example.movieInfo.model.Genre;
import com.example.movieInfo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByDirectorNameContainingIgnoreCase(String directorName);
    List<Movie> findByGenre(Genre genre);
    List<Movie> findByActorsIn(Set<Actor> actor);
    Optional<Object> findByTitleAndReleaseYearAndDirectorName(String title, int releaseYear, String director);
}

