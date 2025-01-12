package com.example.movieInfo.service;

import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.ReviewMapper;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.Review;
import com.example.movieInfo.model.User;
import com.example.movieInfo.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private WatchedMovieService watchedMovieService;
    @Autowired
    private ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse addReview(ReviewRequest reviewRequest) {
        User user = userService.findById(reviewRequest.getUserId());
        Movie movie = movieService.findById(reviewRequest.getMovieId());

        Review review = reviewMapper.toReview(reviewRequest);
        review.setUser(user);
        review.setMovie(movie);

        review = reviewRepository.save(review);

        WatchedMovieRequest watchedMovieRequest = new WatchedMovieRequest(reviewRequest.getUserId(), reviewRequest.getMovieId());
        watchedMovieService.markMovieAsWatched(watchedMovieRequest);

        return reviewMapper.toReviewResponse(review);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        existingReview.setRating((double) reviewRequest.getRating());
        existingReview.setComment(reviewRequest.getComment());
        reviewRepository.save(existingReview);

        return reviewMapper.toReviewResponse(existingReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

