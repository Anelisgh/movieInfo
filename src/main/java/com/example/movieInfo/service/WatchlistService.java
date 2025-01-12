package com.example.movieInfo.service;

import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.WatchlistMapper;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.Watchlist;
import com.example.movieInfo.repository.MovieRepository;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WatchlistMapper watchlistMapper;

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Set<Movie> findMoviesByIdsOrThrow(Set<Long> movieIds) {
        return movieIds.stream()
                .map(movieId -> movieRepository.findById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId)))
                .collect(Collectors.toSet());
    }

    private Watchlist findWatchlistByIdOrThrow(Long id) {
        return watchlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found with id: " + id));
    }

    private void checkForDuplicateWatchlist(String name, User user) {
        boolean exists = watchlistRepository.findByNameAndUser(name, user).isPresent();
        if (exists) {
            throw new DuplicateResourceException("Watchlist already exists with name: " + name + " for user: " + user.getId());
        }
    }

    public WatchlistResponse createWatchlist(WatchlistRequest watchlistRequest) {
        User user = findUserByIdOrThrow(watchlistRequest.getUserId());
        checkForDuplicateWatchlist(watchlistRequest.getName(), user);
        Set<Movie> movies = findMoviesByIdsOrThrow(watchlistRequest.getMovieIds());
        Watchlist watchlist = new Watchlist(watchlistRequest.getName(), user, movies);
        Watchlist savedWatchlist = watchlistRepository.save(watchlist);
        return watchlistMapper.toResponseDTO(savedWatchlist);
    }

    public WatchlistResponse getWatchlistById(Long id) {
        Watchlist watchlist = findWatchlistByIdOrThrow(id);
        return watchlistMapper.toResponseDTO(watchlist);
    }

    public WatchlistResponse updateWatchlist(Long id, WatchlistRequest watchlistRequest) {
        Watchlist existingWatchlist = findWatchlistByIdOrThrow(id);
        existingWatchlist.setName(watchlistRequest.getName());
        User user = findUserByIdOrThrow(watchlistRequest.getUserId());
        checkForDuplicateWatchlist(watchlistRequest.getName(), user);
        existingWatchlist.setUser(user);
        Set<Movie> movies = findMoviesByIdsOrThrow(watchlistRequest.getMovieIds());
        existingWatchlist.setMovies(movies);
        Watchlist updatedWatchlist = watchlistRepository.save(existingWatchlist);
        return watchlistMapper.toResponseDTO(updatedWatchlist);
    }

    public void deleteWatchlist(Long id) {
        Watchlist watchlist = findWatchlistByIdOrThrow(id);
        watchlistRepository.delete(watchlist);
    }
}
