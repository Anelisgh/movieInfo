package com.example.movieInfo.service;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.exception.UsernameNotFoundException;
import com.example.movieInfo.mapper.WatchedMovieMapper;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.WatchedMovies;
import com.example.movieInfo.repository.MovieRepository;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.repository.WatchedMovieRepository;
import com.example.movieInfo.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WatchedMovieService {
    @Autowired
    private WatchedMovieRepository watchedMovieRepository;
    @Autowired
    private WatchedMovieMapper watchedMovieMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public void markMovieAsWatched(WatchedMovieRequest watchedMovieRequest) {
        // Obține ID-ul utilizatorului autentificat și atribuie-l în request
        Long userId = getAuthenticatedUserId();
        watchedMovieRequest.setUserId(userId);

        // Convertire și operațiuni asupra entităților
        WatchedMovies watchedMovie = watchedMovieMapper.toEntity(watchedMovieRequest);
        WatchedMovies existingWatchedMovie = watchedMovieRepository.findByUserAndMovie(watchedMovie.getUser(), watchedMovie.getMovie())
                .orElse(watchedMovie);
        existingWatchedMovie.setIsWatched(true);
        watchedMovieRepository.save(existingWatchedMovie);
    }

    @Transactional
    public void unmarkMovieAsWatched(Long movieId) {
        Long userId = getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Găsește înregistrarea pentru filmul și utilizatorul curent
        WatchedMovies watchedMovie = watchedMovieRepository.findByUserAndMovieId(user, movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found in watched list"));

        // Șterge înregistrarea
        watchedMovieRepository.delete(watchedMovie);
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByName(userDetails.getUsername());
            if (userOptional.isPresent()) {
                return userOptional.get().getId();
            }
        }
        throw new ResourceNotFoundException("Authenticated user not found");
    }

    public boolean isMovieWatched(Long movieId) {
        Long userId = getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return watchedMovieRepository.existsByUserAndMovieIdAndIsWatchedIsTrue(user, movieId);
    }

    public boolean isMovieInAnyWatchlist(Long movieId) {
        Long userId = getAuthenticatedUserId();
        return watchlistRepository.existsByUserIdAndMoviesId(userId, movieId);
    }

    @Transactional
    public void toggleMovieWatchedStatus(Long movieId) {
        Long userId = getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verifică dacă filmul este deja în lista de vizionate
        Optional<WatchedMovies> existingWatchedMovie = watchedMovieRepository.findByUserAndMovieId(user, movieId);

        if (existingWatchedMovie.isPresent()) {
            // Dacă filmul este deja în lista de vizionate, îl eliminăm
            watchedMovieRepository.delete(existingWatchedMovie.get());
        } else {
            // Dacă filmul nu este în lista de vizionate, îl adăugăm
            WatchedMovies watchedMovie = new WatchedMovies();
            watchedMovie.setUser(user);
            watchedMovie.setMovie(movieRepository.findById(movieId)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found")));
            watchedMovie.setIsWatched(true);
            watchedMovieRepository.save(watchedMovie);
        }
    }
}
