package com.example.movieInfo.controller;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.service.WatchedMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WatchedMovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WatchedMovieService watchedMovieService;

    @InjectMocks
    private WatchedMovieController watchedMovieController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(watchedMovieController).build();
    }

    private WatchedMovieRequest createWatchedMovieRequest(Long userId, Long movieId) {
        return new WatchedMovieRequest(userId, movieId);
    }

    @Test
    public void testMarkAsWatched() throws Exception {
        WatchedMovieRequest watchedMovieRequest = createWatchedMovieRequest(1L, 1L);

        doNothing().when(watchedMovieService).markMovieAsWatched(any(WatchedMovieRequest.class));

        mockMvc.perform(post("/watched-movies/mark-as-watched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"movieId\": 1}"))
                .andExpect(status().isOk());
    }
}
