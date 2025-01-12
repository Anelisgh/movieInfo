package com.example.movieInfo.service;
import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.model.*;
import com.example.movieInfo.repository.*;
import com.example.movieInfo.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @Mock
    private WatchedMovieService watchedMovieService;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ReviewRequest createReviewRequest() {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setUserId(1L);
        reviewRequest.setMovieId(1L);
        reviewRequest.setRating(5);
        reviewRequest.setComment("Amazing movie!");
        return reviewRequest;
    }

    private Review createReview() {
        Review review = new Review();
        review.setRating(5.0);
        review.setComment("Amazing movie!");
        return review;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        return user;
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        return movie;
    }

    @Test
    public void testAddReview() {
        ReviewRequest reviewRequest = createReviewRequest();
        User user = createUser();
        Movie movie = createMovie();
        Review review = createReview();
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setRating(5.0);
        reviewResponse.setComment("Amazing movie!");

        when(userService.findById(reviewRequest.getUserId())).thenReturn(user);
        when(movieService.findById(reviewRequest.getMovieId())).thenReturn(movie);
        when(reviewMapper.toReview(reviewRequest)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toReviewResponse(review)).thenReturn(reviewResponse);

        ReviewResponse result = reviewService.addReview(reviewRequest);

        assertNotNull(result);
        assertEquals(5.0, result.getRating());
        assertEquals("Amazing movie!", result.getComment());
        verify(reviewRepository, times(1)).save(review);
        verify(watchedMovieService, times(1)).markMovieAsWatched(any(WatchedMovieRequest.class));
    }


    @Test
    public void testUpdateReview() {
        ReviewRequest reviewRequest = createReviewRequest();
        Review existingReview = createReview();
        existingReview.setId(1L);
        existingReview.setRating(4.0);
        existingReview.setComment("Good movie!");

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setRating(5.0);
        reviewResponse.setComment("Amazing movie!");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));
        when(reviewMapper.toReviewResponse(existingReview)).thenReturn(reviewResponse);

        ReviewResponse result = reviewService.updateReview(1L, reviewRequest);

        assertNotNull(result);
        assertEquals(5.0, result.getRating());
        assertEquals("Amazing movie!", result.getComment());
        verify(reviewRepository, times(1)).save(existingReview);
    }


    @Test
    public void testUpdateReview_ReviewNotFound() {
        ReviewRequest reviewRequest = createReviewRequest();

        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.updateReview(1L, reviewRequest);
        });
    }

    @Test
    public void testDeleteReview() {
        Long reviewId = 1L;

        doNothing().when(reviewRepository).deleteById(reviewId);

        reviewService.deleteReview(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }
}

