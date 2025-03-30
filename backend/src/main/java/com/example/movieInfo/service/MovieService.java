package com.example.movieInfo.service;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.exception.UsernameNotFoundException;
import com.example.movieInfo.mapper.MovieDetailsMapper;
import com.example.movieInfo.mapper.MovieMapper;
import com.example.movieInfo.mapper.ReviewMapper;
import com.example.movieInfo.model.*;
import com.example.movieInfo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private WatchedMovieRepository watchedMovieRepository;
    @Autowired
    private MovieDetailsMapper movieDetailsMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewMapper reviewMapper;

    private Movie findMovieByIdOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    private List<MovieResponse> mapMoviesToResponses(List<Movie> movies) {
        return movies.stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void checkForDuplicateMovie(String title, int releaseYear, String director) {
        boolean exists = movieRepository.findByTitleAndReleaseYearAndDirectorName(title, releaseYear, director).isPresent();
        if (exists) {
            throw new DuplicateResourceException("Movie already exists with title: " + title + ", release year: " + releaseYear + " and director: " + director);
        }
    }

    public Movie findById(Long id) {
        return findMovieByIdOrThrow(id);
    }

    @Transactional(readOnly = true)
    public MovieResponse getMovieResponse(Long id) {
        Movie movie = findMovieByIdOrThrow(id);
        return movieMapper.toResponse(movie);
    }

    private Director findOrCreateDirector(String directorName) {
        return directorRepository.findByName(directorName)
                .orElseGet(() -> {
                    Director newDirector = new Director();
                    newDirector.setName(directorName);
                    return directorRepository.save(newDirector);
                });
    }

    private Set<Actor> findOrCreateActors(Set<String> actorNames) {
        return actorNames.stream()
                .map(name -> actorRepository.findByName(name)
                        .orElseGet(() -> {
                            Actor newActor = new Actor();
                            newActor.setName(name);
                            return actorRepository.save(newActor);
                        }))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Movie addMovie(MovieRequest movieRequest) {
        checkForDuplicateMovie(movieRequest.getTitle(), movieRequest.getReleaseYear(), movieRequest.getDirector());
        Director director = findOrCreateDirector(movieRequest.getDirector());
        Set<Actor> actors = findOrCreateActors(movieRequest.getActors());
        Movie movie = movieMapper.toEntity(movieRequest);
        movie.setDirector(director);
        movie.setActors(actors);
        movie.setPhotoUrl(movieRequest.getPhotoUrl());
        return movieRepository.save(movie);
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> searchByTitle(String title) {
        System.out.println("Searching for title: " + title);
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
        System.out.println("Found movies: " + movies.size());
        return mapMoviesToResponses(movies);
    }

    public MovieUserDetails getMovieDetails(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        MovieUserDetails dto = movieDetailsMapper.toDetailsDTO(movie);

        // Filtrează recenziile publice și mapează-le la ReviewResponse
        List<ReviewResponse> publicReviews = movie.getReviews().stream()
                .filter(review -> review.getReviewType() == ReviewType.PUBLIC)
                .map(review -> {
                    ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
                    reviewResponse.setUserId(review.getUser().getId());
                    reviewResponse.setUsername(review.getUser().getName()); // Adaugă numele utilizatorului
                    return reviewResponse;
                })
                .collect(Collectors.toList());

        // Calculează rating-ul mediu
        double averageRating = publicReviews.stream()
                .mapToDouble(ReviewResponse::getRating)
                .average()
                .orElse(0.0); // Dacă nu există recenzii, rating-ul mediu este 0.0

        dto.setReviews(publicReviews);
        dto.setAverageRating(averageRating);
        return dto;
    }

    private boolean mapWatchedStatus(Movie movie, User user) {
        return watchedMovieRepository.findByUserAndMovie(user, movie).isPresent();
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getRecommendedMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Movie> recommended;

        // Check if the user is not authenticated or is anonymous
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            // Return random movies for unauthenticated users
            List<Movie> allMovies = movieRepository.findAll();
            Collections.shuffle(allMovies);
            recommended = allMovies.stream()
                    .limit(16)
                    .collect(Collectors.toList());
            Collections.shuffle(recommended); // Ensure random order
        } else {
            // Existing logic for authenticated users
            String username = authentication.getName();
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Set<Movie> watchedMovies = watchedMovieRepository.findByUser(user)
                    .stream()
                    .map(WatchedMovies::getMovie)
                    .collect(Collectors.toSet());

            List<Genre> topGenres = watchedMovies.stream()
                    .collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<Genre, Long>comparingByValue().reversed())
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            List<Director> topDirectors = watchedMovies.stream()
                    .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<Director, Long>comparingByValue().reversed())
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            List<Movie> allMovies = movieRepository.findAll();
            List<Movie> unwatchedMovies = allMovies.stream()
                    .filter(movie -> !watchedMovies.contains(movie))
                    .collect(Collectors.toList());

            Map<Movie, Integer> movieScores = new HashMap<>();
            final int CURRENT_YEAR = Year.now().getValue();

            for (Movie movie : unwatchedMovies) {
                int score = 0;

                if (!topGenres.isEmpty()) {
                    if (topGenres.contains(movie.getGenre())) {
                        score += 10;
                        if (movie.getGenre().equals(topGenres.get(0))) {
                            score += 5;
                        }
                    }
                }

                if (!topDirectors.isEmpty()) {
                    if (topDirectors.contains(movie.getDirector())) {
                        score += 8;
                        if (movie.getDirector().equals(topDirectors.get(0))) {
                            score += 4;
                        }
                    }
                }

                if (movie.getReleaseYear() != null) {
                    int yearDiff = CURRENT_YEAR - movie.getReleaseYear();
                    if (yearDiff <= 5) {
                        score += Math.max(0, 5 - yearDiff);
                    }
                }

                movieScores.put(movie, score);
            }

            recommended = movieScores.entrySet().stream()
                    .sorted(Map.Entry.<Movie, Integer>comparingByValue().reversed())
                    .limit(16)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            Collections.shuffle(recommended); // Existing shuffle for diversity
        }

        return mapMoviesToResponses(recommended);
    }
}

//    public MovieResponse updateMovie(Long id, MovieRequest movieRequest) {
//        Movie existingMovie = findMovieByIdOrThrow(id);
//
//        existingMovie.setTitle(movieRequest.getTitle());
//        existingMovie.setDescription(movieRequest.getDescription());
//        existingMovie.setReleaseYear(movieRequest.getReleaseYear());
//        existingMovie.setGenre(movieRequest.getGenre());
//        existingMovie.setPhotoUrl(movieRequest.getPhotoUrl());
//        Director director = findDirectorByNameOrThrow(movieRequest.getDirector());
//        existingMovie.setDirector(director);
//        Set<Actor> actors = findActorsByNameOrThrow(movieRequest.getActors());
//        existingMovie.setActors(actors);
//
//        Movie updatedMovie = movieRepository.save(existingMovie);
//        return movieMapper.toResponse(updatedMovie);
//    }
//
//    @Transactional
//    public void deleteMovie(Long id) {
//        Movie movie = findMovieByIdOrThrow(id);
//        for (WatchedMovies watchedMovie : movie.getWatchedMovies()) {
//            watchedMovieRepository.delete(watchedMovie);
//        }
//        movie.getWatchedMovies().clear();
//        for (Watchlist watchlist : movie.getWatchlists()) {
//            watchlist.getMovies().remove(movie);
//        }
//        movie.getWatchlists().clear();
//        movie.getReviews().clear();
//        movieRepository.delete(movie);
//    }