package com.example.movieInfo.controller;

import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.service.ReviewService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    private ReviewRequest createReviewRequest(Long userId, Long movieId, int rating, String comment) {
        return new ReviewRequest(userId, movieId, rating, comment);
    }

    private ReviewResponse createReviewResponse(Long id, Double rating, String comment, Long userId, Long movieId) {
        return new ReviewResponse(id, rating, comment, userId, movieId);
    }

    @Test
    public void testAddReview() throws Exception {
        ReviewRequest reviewRequest = createReviewRequest(1L, 1L, 5, "Great movie!");
        ReviewResponse reviewResponse = createReviewResponse(1L, 5.0, "Great movie!", 1L, 1L);

        when(reviewService.addReview(any(ReviewRequest.class))).thenReturn(reviewResponse);

        mockMvc.perform(post("/reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"movieId\": 1, \"rating\": 5, \"comment\": \"Great movie!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\": 1, \"rating\": 5.0, \"comment\": \"Great movie!\", \"userId\": 1, \"movieId\": 1}"));
    }

    @Test
    public void testUpdateReview() throws Exception {
        ReviewRequest reviewRequest = createReviewRequest(1L, 1L, 5, "Amazing movie!");
        ReviewResponse reviewResponse = createReviewResponse(1L, 5.0, "Amazing movie!", 1L, 1L);

        when(reviewService.updateReview(anyLong(), any(ReviewRequest.class))).thenReturn(reviewResponse);

        mockMvc.perform(put("/reviews/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"movieId\": 1, \"rating\": 5, \"comment\": \"Amazing movie!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\": 1, \"rating\": 5.0, \"comment\": \"Amazing movie!\", \"userId\": 1, \"movieId\": 1}"));
    }

    @Test
    public void testDeleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(anyLong());

        mockMvc.perform(delete("/reviews/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
