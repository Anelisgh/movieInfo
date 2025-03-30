package com.example.movieInfo.controller;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.model.Genre;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.service.MovieService;
import com.example.movieInfo.service.UserService;
import com.example.movieInfo.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieDetails(@PathVariable Long id) {
        try {
            MovieUserDetails movieDetails = movieService.getMovieDetails(id);
            return ResponseEntity.ok(movieDetails);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<MovieResponse> addMovie(@RequestBody @Valid MovieRequest movieRequest) {
        Movie movie = movieService.addMovie(movieRequest);
        MovieResponse response = movieService.getMovieResponse(movie.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchByTitle(title));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<MovieResponse>> getRecommendedMovies() {
        List<MovieResponse> recommendations = movieService.getRecommendedMovies();
        return ResponseEntity.ok(recommendations);
    }
    @GetMapping("/genres")
    public ResponseEntity<Genre[]> getGenres() {
        return ResponseEntity.ok(Genre.values());
    }

}
