package com.example.movieInfo.controller;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.service.WatchedMovieService;
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

    @PostMapping("/mark-as-watched")
    public ResponseEntity<Void> markAsWatched(@RequestBody @Valid WatchedMovieRequest watchedMovieRequest) {
        watchedMovieService.markMovieAsWatched(watchedMovieRequest);
        return ResponseEntity.ok().build();
    }
}
