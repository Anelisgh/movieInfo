package com.example.movieInfo.service;

import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.mapper.WatchlistMapper;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.Watchlist;
import com.example.movieInfo.repository.MovieRepository;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.repository.WatchlistRepository;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WatchlistServiceTest {

    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WatchlistMapper watchlistMapper;

    @InjectMocks
    private WatchlistService watchlistService;

    private WatchlistRequest watchlistRequest;
    private WatchlistResponse watchlistResponse;
    private User user;
    private Movie movie;
    private Watchlist watchlist;

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

        Set<Movie> movies = new HashSet<>();
        movies.add(movie);

        watchlist = new Watchlist("My Watchlist", user, movies);

        Set<String> movieTitles = new HashSet<>();
        movieTitles.add("Inception");

        watchlistRequest = new WatchlistRequest(1L, "My Watchlist", 1L, Set.of(1L));
        watchlistResponse = new WatchlistResponse("My Watchlist", movieTitles);
    }

    @Test
    public void testCreateWatchlist() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(watchlistMapper.toResponseDTO(any(Watchlist.class))).thenReturn(watchlistResponse);
        when(watchlistRepository.findByNameAndUser("My Watchlist", user)).thenReturn(Optional.empty());
        when(watchlistRepository.save(any(Watchlist.class))).thenReturn(watchlist);
        WatchlistResponse result = watchlistService.createWatchlist(watchlistRequest);
        verify(watchlistRepository, times(1)).save(any(Watchlist.class));
        assertEquals("My Watchlist", result.getName());
        assertTrue(result.getMovieTitles().contains("Inception"));
    }

    @Test
    public void testCreateWatchlist_AlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(watchlistRepository.findByNameAndUser("My Watchlist", user)).thenReturn(Optional.of(watchlist));
        assertThrows(DuplicateResourceException.class, () -> watchlistService.createWatchlist(watchlistRequest));
    }

    @Test
    public void testGetWatchlistById() {
        when(watchlistRepository.findById(1L)).thenReturn(Optional.of(watchlist));
        when(watchlistMapper.toResponseDTO(any(Watchlist.class))).thenReturn(watchlistResponse);
        WatchlistResponse result = watchlistService.getWatchlistById(1L);
        assertEquals("My Watchlist", result.getName());
        assertTrue(result.getMovieTitles().contains("Inception"));
    }

    @Test
    public void testGetWatchlistById_NotFound() {
        when(watchlistRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> watchlistService.getWatchlistById(1L));
    }

    @Test
    public void testUpdateWatchlist() {
        User user = new User();
        user.setId(1L);
        user.setName("John Smith");
        user.setEmail("john.smith@example.com");

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Movie 1");

        Watchlist existingWatchlist = new Watchlist("My Watchlist", user, Set.of(movie));
        existingWatchlist.setId(1L);
        when(watchlistRepository.findById(1L)).thenReturn(Optional.of(existingWatchlist));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(watchlistRepository.save(any(Watchlist.class))).thenReturn(existingWatchlist);
        when(watchlistMapper.toResponseDTO(any(Watchlist.class)))
                .thenReturn(new WatchlistResponse("Updated Watchlist", Set.of("Movie 1")));
        WatchlistRequest updateRequest = new WatchlistRequest(1L, "Updated Watchlist", 1L, Set.of(1L));
        WatchlistResponse updatedWatchlist = watchlistService.updateWatchlist(1L, updateRequest);
        assertEquals("Updated Watchlist", updatedWatchlist.getName());
        assertTrue(updatedWatchlist.getMovieTitles().contains("Movie 1"));
    }


    @Test
    public void testUpdateWatchlist_AlreadyExists() {
        when(watchlistRepository.findById(1L)).thenReturn(Optional.of(watchlist));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(watchlistRepository.findByNameAndUser("Updated Watchlist", user)).thenReturn(Optional.of(watchlist));
        WatchlistRequest updateRequest = new WatchlistRequest(1L, "Updated Watchlist", 1L, Set.of(1L));
        assertThrows(DuplicateResourceException.class, () -> watchlistService.updateWatchlist(1L, updateRequest));
    }

    @Test
    public void testDeleteWatchlist() {
        when(watchlistRepository.findById(1L)).thenReturn(Optional.of(watchlist));
        watchlistService.deleteWatchlist(1L);
        verify(watchlistRepository, times(1)).delete(watchlist);
    }

    @Test
    public void testDeleteWatchlist_NotFound() {
        when(watchlistRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> watchlistService.deleteWatchlist(1L));
    }
}
