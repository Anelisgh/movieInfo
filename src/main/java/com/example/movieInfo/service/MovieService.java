package com.example.movieInfo.service;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.MovieDetailsMapper;
import com.example.movieInfo.mapper.MovieMapper;
import com.example.movieInfo.model.*;
import com.example.movieInfo.repository.ActorRepository;
import com.example.movieInfo.repository.DirectorRepository;
import com.example.movieInfo.repository.MovieRepository;
import com.example.movieInfo.repository.WatchedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private Movie findMovieByIdOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    private Director findDirectorByNameOrThrow(String directorName) {
        return directorRepository.findByName(directorName)
                .orElseThrow(() -> new ResourceNotFoundException("Director not found with name: " + directorName));
    }

    private Set<Actor> findActorsByNameOrThrow(Set<String> actorNames){
        return actorNames.stream()
                .map(actorName -> actorRepository.findByName(actorName)
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with name: " + actorName)))
                .collect(Collectors.toSet());
    }

    private List<MovieResponse> mapMoviesToResponses(List<Movie> movies) {
        return movies.stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    private List<Movie> sortMoviesByReleaseYear(List<Movie> movies) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getReleaseYear))
                .collect(Collectors.toList());
    }

    private List<MovieResponse> searchMoviesByCriteria(Function<String, List<Movie>> searchFunction, String criteria) {
        List<Movie> movies = searchFunction.apply(criteria);
        if (movies.isEmpty()) {
            throw new ResourceNotFoundException("No movies found with criteria: " + criteria);
        }
        return mapMoviesToResponses(movies);
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

    public Movie addMovie(MovieRequest movieRequest) {
        checkForDuplicateMovie(movieRequest.getTitle(), movieRequest.getReleaseYear(), movieRequest.getDirector());
        Director director = findOrCreateDirector(movieRequest.getDirector());
        Set<Actor> actors = findOrCreateActors(movieRequest.getActors());
        Movie movie = movieMapper.toEntity(movieRequest);
        movie.setDirector(director);
        movie.setActors(actors);

        return movieRepository.save(movie);
    }


    public MovieResponse updateMovie(Long id, MovieRequest movieRequest) {
        Movie existingMovie = findMovieByIdOrThrow(id);

        existingMovie.setTitle(movieRequest.getTitle());
        existingMovie.setDescription(movieRequest.getDescription());
        existingMovie.setReleaseYear(movieRequest.getReleaseYear());
        existingMovie.setGenre(movieRequest.getGenre());

        Director director = findDirectorByNameOrThrow(movieRequest.getDirector());
        existingMovie.setDirector(director);
        Set<Actor> actors = findActorsByNameOrThrow(movieRequest.getActors());
        existingMovie.setActors(actors);

        Movie updatedMovie = movieRepository.save(existingMovie);
        return movieMapper.toResponse(updatedMovie);
    }

    public void deleteMovie(Long id) {
        Movie movie = findMovieByIdOrThrow(id);
        for (WatchedMovies watchedMovie : movie.getWatchedMovies()) {
            watchedMovieRepository.delete(watchedMovie);
        }
        movie.getWatchedMovies().clear();
        for (Watchlist watchlist : movie.getWatchlists()) {
            watchlist.getMovies().remove(movie);
        }
        movie.getWatchlists().clear();
        movie.getReviews().clear();
        movieRepository.delete(movie);
    }

    public List<MovieResponse> searchByTitle(String title) {
        return searchMoviesByCriteria(movieRepository::findByTitleContainingIgnoreCase, title);
    }

    public List<MovieResponse> searchByDirector(String director) {
        return searchMoviesByCriteria(movieRepository::findByDirectorNameContainingIgnoreCase, director);
    }

    public List<MovieResponse> searchByGenre(String genre) {
        Genre genreEnum = Genre.valueOf(genre.toUpperCase());
        return mapMoviesToResponses(movieRepository.findByGenre(genreEnum));
    }

    public List<MovieResponse> searchByActors(String actorNames) {
        String[] actorArray = actorNames.split(",");
        Set<Actor> actors = Arrays.stream(actorArray)
                .map(String::trim)
                .map(name -> actorRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with name: " + name)))
                .collect(Collectors.toSet());

        List<Movie> movies = movieRepository.findByActorsIn(actors);
        return mapMoviesToResponses(movies);
    }

    public List<MovieResponse> getAllMoviesSortedByReleaseYear() {
        List<Movie> movies = movieRepository.findAll();
        return mapMoviesToResponses(sortMoviesByReleaseYear(movies));
    }

    public List<MovieUserDetails> getMovies(User user) {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movie -> {
                    MovieUserDetails dto = movieDetailsMapper.toDetailsDTO(movie);
                    dto.setWatched(mapWatchedStatus(movie, user));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return mapMoviesToResponses(movies);
    }

    private boolean mapWatchedStatus(Movie movie, User user) {
        return watchedMovieRepository.findByUserAndMovie(user, movie).isPresent();
    }
}
