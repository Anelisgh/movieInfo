package com.example.movieInfo.controller;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.model.Genre;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.service.MovieService;
import com.example.movieInfo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
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

    private MovieResponse createMovieResponse(String title, String description, int releaseYear, String genre, String directorName, String actorName) {
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setTitle(title);
        movieResponse.setDescription(description);
        movieResponse.setReleaseYear(releaseYear);
        movieResponse.setGenre(genre);
        movieResponse.setDirectorName(directorName);
        movieResponse.setActors(Collections.singleton(actorName));
        return movieResponse;
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieRequest movieRequest = createMovieRequest("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");
        Movie movie = new Movie();
        movie.setId(1L);
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.addMovie(any(MovieRequest.class))).thenReturn(movie);
        when(movieService.getMovieResponse(anyLong())).thenReturn(movieResponse);

        mockMvc.perform(post("/movies/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"director\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}"));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        MovieRequest movieRequest = createMovieRequest("Inception", "A thief who steals corporate secrets...", 2010, Genre.SCI_FI, "Christopher Nolan", "Leonardo DiCaprio");
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.updateMovie(anyLong(), any(MovieRequest.class))).thenReturn(movieResponse);

        mockMvc.perform(put("/movies/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"director\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}"));
    }

    @Test
    public void testDeleteMovie() throws Exception {
        doNothing().when(movieService).deleteMovie(anyLong());

        mockMvc.perform(delete("/movies/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSearchMoviesByTitle() throws Exception {
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.searchByTitle(anyString())).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies/search")
                        .param("title", "Inception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}]"));
    }

    @Test
    public void testSearchMoviesByDirector() throws Exception {
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.searchByDirector(anyString())).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies/search")
                        .param("director", "Christopher Nolan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}]"));
    }

    @Test
    public void testSearchMoviesByGenre() throws Exception {
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.searchByGenre(anyString())).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies/search")
                        .param("genre", "SCI_FI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}]"));
    }

    @Test
    public void testSearchMoviesByActor() throws Exception {
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.searchByActors(anyString())).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies/search")
                        .param("actor", "Leonardo DiCaprio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}]"));
    }

    @Test
    public void testSearchMoviesNoCriteria() throws Exception {
        mockMvc.perform(get("/movies/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllMoviesSortedByReleaseYear() throws Exception {
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.getAllMoviesSortedByReleaseYear()).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies/sorted-by-year")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}]"));
    }

    @Test
    public void testGetMovies() throws Exception {
        MovieUserDetails movieUserDetails = new MovieUserDetails();
        movieUserDetails.setTitle("Inception");

        User user = new User();
        user.setId(1L);

        when(userService.findById(anyLong())).thenReturn(user);
        when(movieService.getMovies(any(User.class))).thenReturn(Collections.singletonList(movieUserDetails));

        mockMvc.perform(get("/movies/get_details/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\"}]"));
    }

    @Test
    public void testGetAllMovies() throws Exception {
        MovieResponse movieResponse = createMovieResponse("Inception", "A thief who steals corporate secrets...", 2010, "SCI_FI", "Christopher Nolan", "Leonardo DiCaprio");

        when(movieService.getAllMovies()).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"title\": \"Inception\", \"description\": \"A thief who steals corporate secrets...\", \"releaseYear\": 2010, \"genre\": \"SCI_FI\", \"directorName\": \"Christopher Nolan\", \"actors\": [\"Leonardo DiCaprio\"]}]"));
    }
}
