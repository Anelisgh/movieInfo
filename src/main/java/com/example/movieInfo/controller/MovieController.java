package com.example.movieInfo.controller;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.service.MovieService;
import com.example.movieInfo.service.UserService;
import com.example.movieInfo.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<MovieResponse> addMovie(@RequestBody @Valid MovieRequest movieRequest) {
        Movie movie = movieService.addMovie(movieRequest);
        MovieResponse response = movieService.getMovieResponse(movie.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieRequest movieRequest) {
        MovieResponse updatedMovie = movieService.updateMovie(id, movieRequest);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(@RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String director,
                                                            @RequestParam(required = false) String genre,
                                                            @RequestParam(required = false) String actor) {
        if (title != null) {
            return ResponseEntity.ok(movieService.searchByTitle(title));
        } else if (director != null) {
            return ResponseEntity.ok(movieService.searchByDirector(director));
        } else if (genre != null) {
            return ResponseEntity.ok(movieService.searchByGenre(genre));
        } else if (actor != null) {
            return ResponseEntity.ok(movieService.searchByActors(actor));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/sorted-by-year")
    public ResponseEntity<List<MovieResponse>> getAllMoviesSortedByReleaseYear() {
        List<MovieResponse> response = movieService.getAllMoviesSortedByReleaseYear();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_details/{userId}")
    public ResponseEntity<List<MovieUserDetails>> getMovies(@PathVariable Long userId) {
        User user = userService.findById(userId);
        List<MovieUserDetails> response = movieService.getMovies(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        List<MovieResponse> response = movieService.getAllMovies();
        return ResponseEntity.ok(response);
    }
}
