package com.example.movieInfo.controller;

import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchlists")
@Validated
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    public ResponseEntity<WatchlistResponse> createWatchlist(@RequestBody @Valid WatchlistRequest watchlistRequest) {
        WatchlistResponse responseDTO = watchlistService.createWatchlist(watchlistRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<WatchlistResponse> getWatchlistById(@PathVariable Long id) {
        WatchlistResponse responseDTO = watchlistService.getWatchlistById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<WatchlistResponse> updateWatchlist(@PathVariable Long id, @RequestBody @Valid WatchlistRequest watchlistRequest) {
        watchlistRequest.setId(id);
        WatchlistResponse responseDTO = watchlistService.updateWatchlist(id, watchlistRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable Long id) {
        watchlistService.deleteWatchlist(id);
        return ResponseEntity.noContent().build();
    }
}
