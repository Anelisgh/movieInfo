package com.example.movieInfo.service;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.mapper.WatchedMovieMapper;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.WatchedMovies;
import com.example.movieInfo.repository.WatchedMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class WatchedMovieServiceTest {

    @Mock
    private WatchedMovieRepository watchedMovieRepository;

    @Mock
    private WatchedMovieMapper watchedMovieMapper;

    @InjectMocks
    private WatchedMovieService watchedMovieService;

    private WatchedMovieRequest watchedMovieRequest;
    private User user;
    private Movie movie;
    private WatchedMovies watchedMovies;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("John Smith");
        user.setEmail("john.smith@example.com");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        watchedMovies = new WatchedMovies();
        watchedMovies.setUser(user);
        watchedMovies.setMovie(movie);
        watchedMovies.setWatched(false);

        watchedMovieRequest = new WatchedMovieRequest(1L, 1L);
    }

    @Test
    public void testMarkMovieAsWatched_NewWatchedMovie() {
        when(watchedMovieMapper.toEntity(watchedMovieRequest)).thenReturn(watchedMovies);
        watchedMovieService.markMovieAsWatched(watchedMovieRequest);
        verify(watchedMovieRepository, times(1)).save(watchedMovies);
    }

    @Test
    public void testMarkMovieAsWatched_ExistingWatchedMovie() {
        when(watchedMovieMapper.toEntity(watchedMovieRequest)).thenReturn(watchedMovies);
        when(watchedMovieRepository.findByUserAndMovie(user, movie)).thenReturn(Optional.of(watchedMovies));
        watchedMovieService.markMovieAsWatched(watchedMovieRequest);
        verify(watchedMovieRepository, times(1)).save(watchedMovies);
        assertTrue(watchedMovies.isWatched());
    }
}

