package com.example.movieInfo.controller;

import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.dto.WatchlistWithStatusResponse;
import com.example.movieInfo.service.WatchedMovieService;
import com.example.movieInfo.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlists")
@Validated
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private WatchedMovieService watchedMovieService;

    @PostMapping("add")
    public ResponseEntity<WatchlistResponse> createWatchlist(@RequestBody @Valid WatchlistRequest watchlistRequest) {
        WatchlistResponse responseDTO = watchlistService.createWatchlist(watchlistRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<WatchlistResponse> getWatchlistById(@PathVariable Long id) {
        WatchlistResponse responseDTO = watchlistService.getWatchlistById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<List<WatchlistResponse>> getUserWatchlists() {
        List<WatchlistResponse> response = watchlistService.getUserWatchlists();
        return ResponseEntity.ok(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<WatchlistResponse> updateWatchlist(
            @PathVariable Long id,
            @RequestBody @Valid WatchlistRequest watchlistRequest
    ) {
        WatchlistResponse responseDTO = watchlistService.updateWatchlist(id, watchlistRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable Long id) {
        watchlistService.deleteWatchlist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contains/{movieId}")
    public ResponseEntity<Boolean> isMovieInAnyWatchlist(@PathVariable Long movieId) {
        boolean exists = watchlistService.isMovieInAnyWatchlist(movieId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/toggle-watched/{movieId}")
    public ResponseEntity<Void> toggleWatchedStatus(@PathVariable Long movieId) {
        watchedMovieService.toggleMovieWatchedStatus(movieId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{watchlistId}/add-movie/{movieId}")
    public ResponseEntity<Void> addMovieToWatchlist(
            @PathVariable Long watchlistId,
            @PathVariable Long movieId
    ) {
        System.out.println("Adding movie ID " + movieId + " to watchlist ID " + watchlistId);
        watchlistService.addMovieToWatchlist(watchlistId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{watchlistId}/remove-movie/{movieId}")
    public ResponseEntity<Void> removeMovieFromWatchlist(
            @PathVariable Long watchlistId,
            @PathVariable Long movieId
    ) {
        watchlistService.removeMovieFromWatchlist(watchlistId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user-with-status/{movieId}")
    public ResponseEntity<List<WatchlistWithStatusResponse>> getUserWatchlistsWithStatus(@PathVariable Long movieId) {
        List<WatchlistWithStatusResponse> response = watchlistService.getUserWatchlistsWithMovieStatus(movieId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sourceId}/move-to/{targetId}")
    public ResponseEntity<Void> moveMovies(
            @PathVariable Long sourceId,
            @PathVariable Long targetId,
            @RequestBody List<Long> movieIds
    ) {
        watchlistService.moveMovies(sourceId, targetId, movieIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{watchlistId}/remove-movies")
    public ResponseEntity<Void> removeMovies(
            @PathVariable Long watchlistId,
            @RequestBody List<Long> movieIds
    ) {
        watchlistService.removeMoviesFromWatchlist(watchlistId, movieIds);
        return ResponseEntity.noContent().build();
    }
}
