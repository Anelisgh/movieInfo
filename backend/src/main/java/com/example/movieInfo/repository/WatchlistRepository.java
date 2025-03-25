package com.example.movieInfo.repository;

import com.example.movieInfo.model.User;
import com.example.movieInfo.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long>{
    Optional<Watchlist> findByNameAndUser(String name, User user);
    List<Watchlist> findByUser(User user);

    boolean existsByNameAndUser(String name, User user);

    boolean existsByUserIdAndMoviesId(Long userId, Long movieId);

    List<Watchlist> findByUserId(Long userId);
}
