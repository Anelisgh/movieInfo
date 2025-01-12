package com.example.movieInfo.controller;

import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@Validated
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody @Valid ReviewRequest reviewRequest) {
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
}
