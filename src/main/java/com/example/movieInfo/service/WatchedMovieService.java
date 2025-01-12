package com.example.movieInfo.service;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.mapper.WatchedMovieMapper;
import com.example.movieInfo.model.WatchedMovies;
import com.example.movieInfo.repository.WatchedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WatchedMovieService {
    @Autowired
    private WatchedMovieRepository watchedMovieRepository;
    @Autowired
    private WatchedMovieMapper watchedMovieMapper;

    @Transactional
    public void markMovieAsWatched(WatchedMovieRequest watchedMovieRequest) {
        WatchedMovies watchedMovie = watchedMovieMapper.toEntity(watchedMovieRequest);
        WatchedMovies existingWatchedMovie = watchedMovieRepository.findByUserAndMovie(watchedMovie.getUser(), watchedMovie.getMovie())
                .orElse(watchedMovie);
        existingWatchedMovie.setWatched(true);
        watchedMovieRepository.save(existingWatchedMovie);
    }
}
