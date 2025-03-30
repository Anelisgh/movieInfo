package com.example.movieInfo.repository;

import com.example.movieInfo.model.Review;
import com.example.movieInfo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Object> findByMovieId(Long movieId);
    Optional<Review> findByUserAndMovieId(User user, Long movieId);
    void deleteAllByUser(User user);
}

