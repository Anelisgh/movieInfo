package com.example.movieInfo.controller;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.service.WatchedMovieService;
import com.example.movieInfo.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watched-movies")
@Validated
public class WatchedMovieController {
    @Autowired
    private WatchedMovieService watchedMovieService;
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/mark-as-watched")
    public ResponseEntity<Void> markAsWatched(@RequestBody @Valid WatchedMovieRequest request) {
        watchedMovieService.markMovieAsWatched(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unmark-as-watched/{movieId}")
    public ResponseEntity<Void> unmarkAsWatched(@PathVariable Long movieId) {
        watchedMovieService.unmarkMovieAsWatched(movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/{movieId}")
    public ResponseEntity<Boolean> isMovieWatched(@PathVariable Long movieId) {
        boolean isWatched = watchedMovieService.isMovieWatched(movieId);
        return ResponseEntity.ok(isWatched);
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
}
