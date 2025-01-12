package com.example.movieInfo.service;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.mapper.MovieDetailsMapper;
import com.example.movieInfo.mapper.MovieMapper;
import com.example.movieInfo.model.*;
import com.example.movieInfo.repository.ActorRepository;
import com.example.movieInfo.repository.DirectorRepository;
import com.example.movieInfo.repository.MovieRepository;
import com.example.movieInfo.repository.WatchedMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private WatchedMovieRepository watchedMovieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private MovieDetailsMapper movieDetailsMapper;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MovieRequest createMovieRequest(String title, String description, int releaseYear, Genre genre, String directorName, String actorName) {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle(title);
        movieRequest.setDescription(description);
        movieRequest.setReleaseYear(releaseYear);
        movieRequest.setGenre(genre);
        movieRequest.setDirector(directorName);
        movieRequest.setActors(Collections.singleton(actorName));
        return movieRequest;
    }

    private Movie createMovie(String title, String description, int releaseYear, Genre genre, String directorName, String actorName) {
        Director director = new Director();
        director.setName(directorName);

        Actor actor = new Actor();
        actor.setName(actorName);

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseYear(releaseYear);
        movie.setGenre(genre);
        movie.setDirector(director);
        movie.setActors(Collections.singleton(actor));
        return movie;
    }

    private void setupCommonMocks(Movie movie) {
        when(directorRepository.findByName(anyString())).thenReturn(Optional.of(movie.getDirector()));
        when(actorRepository.findByName(anyString())).thenReturn(Optional.of(movie.getActors().iterator().next()));
        when(movieMapper.toEntity(any(MovieRequest.class))).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
    }

    @Test
    public void testAddMovie() {
        MovieRequest movieRequest = createMovieRequest("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");
        Movie movie = createMovie("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");

        setupCommonMocks(movie);

        Movie result = movieService.addMovie(movieRequest);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testGetMovieResponse() {
        Movie movie = createMovie("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setTitle("Inception");

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(movieMapper.toResponse(any(Movie.class))).thenReturn(movieResponse);

        MovieResponse result = movieService.getMovieResponse(1L);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateMovie() {
        MovieRequest movieRequest = createMovieRequest("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");

        Movie existingMovie = createMovie("Old Title", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");
        existingMovie.setId(1L);

        Movie updatedMovie = createMovie("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");
        updatedMovie.setId(1L);

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setTitle("Inception");

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(existingMovie));
        setupCommonMocks(updatedMovie);
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
        when(movieMapper.toResponse(any(Movie.class))).thenReturn(movieResponse);

        MovieResponse result = movieService.updateMovie(1L, movieRequest);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setWatchedMovies(new HashSet<>());
        movie.setWatchlists(new HashSet<>());
        movie.setReviews(new ArrayList<>());

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        movieService.deleteMovie(1L);
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    public void testFindById() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));

        Movie result = movieService.findById(1L);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    public void testSearchByGenre() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setGenre(Genre.SCI_FI);

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setTitle("Inception");

        when(movieRepository.findByGenre(any(Genre.class))).thenReturn(Collections.singletonList(movie));
        when(movieMapper.toResponse(any(Movie.class))).thenReturn(movieResponse);

        List<MovieResponse> result = movieService.searchByGenre("SCI_FI");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    public void testSearchByTitle() {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setTitle("Inception");

        when(movieRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Collections.singletonList(movie));
        when(movieMapper.toResponse(any(Movie.class))).thenReturn(movieResponse);

        List<MovieResponse> result = movieService.searchByTitle("Inception");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

}

