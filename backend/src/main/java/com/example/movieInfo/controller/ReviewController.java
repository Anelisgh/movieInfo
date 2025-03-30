package com.example.movieInfo.controller;

import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.exception.UsernameNotFoundException;
import com.example.movieInfo.mapper.ReviewMapper;
import com.example.movieInfo.model.Review;
import com.example.movieInfo.model.User;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/reviews")
@Validated
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        // Obține userId din SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Setează userId în reviewRequest
        reviewRequest.setUserId(user.getId());

        ReviewResponse review = reviewService.addReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewRequest reviewRequest) {
        ReviewResponse updatedReview = reviewService.updateReview(id, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{movieId}")
    public ResponseEntity<?> getUserReviewForMovie(@PathVariable Long movieId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.ok(Collections.emptyMap());
            }

            String username = authentication.getName();
            Review review = reviewService.findByUserAndMovie(username, movieId)
                    .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

            return ResponseEntity.ok(reviewMapper.toReviewResponse(review));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(Collections.emptyMap());
        }
    }
}
