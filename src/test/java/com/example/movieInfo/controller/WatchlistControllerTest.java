package com.example.movieInfo.controller;

import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.service.WatchlistService;
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
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class WatchlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WatchlistService watchlistService;

    @InjectMocks
    private WatchlistController watchlistController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(watchlistController).build();
    }

    private WatchlistRequest createWatchlistRequest(Long id, String name, Long userId, Set<Long> movieIds) {
        return new WatchlistRequest(id, name, userId, movieIds);
    }

    private WatchlistResponse createWatchlistResponse(String name, Set<String> movieTitles) {
        return new WatchlistResponse(name, movieTitles);
    }

    @Test
    public void testCreateWatchlist() throws Exception {
        WatchlistRequest watchlistRequest = createWatchlistRequest(1L, "Favorites", 1L, new HashSet<>(Collections.singletonList(1L)));
        WatchlistResponse watchlistResponse = createWatchlistResponse("Favorites", new HashSet<>(Collections.singletonList("Inception")));

        when(watchlistService.createWatchlist(any(WatchlistRequest.class))).thenReturn(watchlistResponse);

        mockMvc.perform(post("/watchlists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Favorites\", \"userId\": 1, \"movieIds\": [1]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"Favorites\", \"movieTitles\": [\"Inception\"]}"));
    }

    @Test
    public void testGetWatchlistById() throws Exception {
        WatchlistResponse watchlistResponse = createWatchlistResponse("Favorites", new HashSet<>(Collections.singletonList("Inception")));

        when(watchlistService.getWatchlistById(anyLong())).thenReturn(watchlistResponse);

        mockMvc.perform(get("/watchlists/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"Favorites\", \"movieTitles\": [\"Inception\"]}"));
    }

    @Test
    public void testUpdateWatchlist() throws Exception {
        WatchlistRequest watchlistRequest = createWatchlistRequest(1L, "Favorites Updated", 1L, new HashSet<>(Collections.singletonList(1L)));
        WatchlistResponse watchlistResponse = createWatchlistResponse("Favorites Updated", new HashSet<>(Collections.singletonList("Inception")));

        when(watchlistService.updateWatchlist(anyLong(), any(WatchlistRequest.class))).thenReturn(watchlistResponse);

        mockMvc.perform(put("/watchlists/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Favorites Updated\", \"userId\": 1, \"movieIds\": [1]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"Favorites Updated\", \"movieTitles\": [\"Inception\"]}"));
    }

    @Test
    public void testDeleteWatchlist() throws Exception {
        doNothing().when(watchlistService).deleteWatchlist(anyLong());

        mockMvc.perform(delete("/watchlists/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
