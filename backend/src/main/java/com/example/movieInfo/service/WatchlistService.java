package com.example.movieInfo.service;

import com.example.movieInfo.dto.MovieDetails;
import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.dto.WatchlistWithStatusResponse;
import com.example.movieInfo.exception.AccessDeniedException;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.exception.UsernameNotFoundException;
import com.example.movieInfo.mapper.WatchlistMapper;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.Watchlist;
import com.example.movieInfo.repository.MovieRepository;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WatchlistMapper watchlistMapper;
    @Autowired
    private WatchedMovieService watchedMovieService;

    private Long getAuthenticatedUserId() {
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

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Set<Movie> findMoviesByIdsOrThrow(Set<Long> movieIds) {
        return movieIds.stream()
                .map(movieId -> movieRepository.findById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId)))
                .collect(Collectors.toSet());
    }

    private Watchlist findWatchlistByIdOrThrow(Long id) {
        return watchlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found with id: " + id));
    }

    private void checkForDuplicateWatchlist(String name, User user) {
        boolean exists = watchlistRepository.findByNameAndUser(name, user).isPresent();
        if (exists) {
            throw new DuplicateResourceException("Watchlist already exists with name: " + name + " for user: " + user.getId());
        }
    }

    public WatchlistResponse createWatchlist(WatchlistRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verifică existența watchlist-ului pentru user-ul curent
        if (watchlistRepository.existsByNameAndUser(request.getName(), user)) {
            throw new DuplicateResourceException("Watchlist already exists");
        }

        // Verifică dacă movieIds conține valori null sau 0
        if (request.getMovieIds() == null || request.getMovieIds().contains(null) || request.getMovieIds().contains(0L)) {
            throw new IllegalArgumentException("Movie IDs must be valid and not null");
        }

        Set<Movie> movies = new HashSet<>(movieRepository.findAllById(request.getMovieIds()));
        Watchlist watchlist = new Watchlist();
        watchlist.setName(request.getName());
        watchlist.setUser(user);
        watchlist.setMovies(movies);

        Watchlist saved = watchlistRepository.save(watchlist);
        return convertToResponse(saved);
    }


    public WatchlistResponse getWatchlistById(Long id) {
        Watchlist watchlist = findWatchlistByIdOrThrow(id);
        return watchlistMapper.toResponseDTO(watchlist);
    }

    public WatchlistResponse updateWatchlist(Long id, WatchlistRequest watchlistRequest) {
        Watchlist existingWatchlist = findWatchlistByIdOrThrow(id);

        // Verifică dacă utilizatorul are drepturi asupra watchlist-ului
        Long userId = getAuthenticatedUserId();
        if (!existingWatchlist.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not authorized to modify this watchlist");
        }

        // Actualizează doar numele
        existingWatchlist.setName(watchlistRequest.getName());

        // Salvează modificările
        Watchlist updatedWatchlist = watchlistRepository.save(existingWatchlist);
        return watchlistMapper.toResponseDTO(updatedWatchlist);
    }

    public void deleteWatchlist(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found"));

        if (!watchlist.getUser().getName().equals(username)) {
            throw new AccessDeniedException("Not authorized to delete this watchlist");
        }

        watchlistRepository.delete(watchlist);
    }

    public List<Watchlist> findByUserName(String userName) {
        Optional<User> user = userRepository.findByName(userName);
        if (user.isPresent()) {
            return watchlistRepository.findByUser(user.get()); // Modificat pentru a folosi user-ul
        }
        return Collections.emptyList();
    }

    public List<WatchlistResponse> getUserWatchlists() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return watchlistRepository.findByUser(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private WatchlistResponse convertToResponse(Watchlist watchlist) {
        WatchlistResponse response = new WatchlistResponse();
        response.setId(watchlist.getId());
        response.setName(watchlist.getName());

        Set<MovieDetails> movies = watchlist.getMovies().stream()
                .map(movie -> {
                    MovieDetails details = new MovieDetails();
                    details.setTitle(movie.getTitle());
                    details.setPhotoUrl(movie.getPhotoUrl());
                    details.setReleaseYear(movie.getReleaseYear());
                    return details;
                })
                .collect(Collectors.toSet());

        response.setMovies(movies);
        return response;
    }
    public boolean isMovieInAnyWatchlist(Long movieId) {
        Long userId = watchedMovieService.getAuthenticatedUserId(); // Apelează metoda publică
        return watchlistRepository.existsByUserIdAndMoviesId(userId, movieId);
    }

    public void addMovieToWatchlist(Long watchlistId, Long movieId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        watchlist.getMovies().add(movie);
        watchlistRepository.save(watchlist);
    }

    public void removeMovieFromWatchlist(Long watchlistId, Long movieId) {
        Watchlist watchlist = findWatchlistByIdOrThrow(watchlistId);
        watchlist.getMovies().removeIf(m -> m.getId().equals(movieId));
        watchlistRepository.save(watchlist);
    }

    public List<WatchlistWithStatusResponse> getUserWatchlistsWithMovieStatus(Long movieId) {
        Long userId = getAuthenticatedUserId();
        List<Watchlist> watchlists = watchlistRepository.findByUserId(userId);

        return watchlists.stream().map(watchlist -> {
            boolean containsMovie = watchlist.getMovies().stream()
                    .anyMatch(m -> m.getId().equals(movieId));
            return new WatchlistWithStatusResponse(watchlist.getId(), watchlist.getName(), containsMovie);
        }).collect(Collectors.toList());
    }

    public void moveMovies(Long sourceId, Long targetId, List<Long> movieIds) {
        // Verifică dacă watchlist-urile există
        Watchlist sourceWatchlist = findWatchlistByIdOrThrow(sourceId);
        Watchlist targetWatchlist = findWatchlistByIdOrThrow(targetId);

        // Verifică dacă utilizatorul are drepturi asupra ambelor watchlist-uri
        Long userId = getAuthenticatedUserId();
        if (!sourceWatchlist.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not authorized to modify source watchlist");
        }
        if (!targetWatchlist.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not authorized to modify target watchlist");
        }

        // Găsește filmele care trebuie mutate
        Set<Movie> moviesToMove = movieIds.stream()
                .map(movieId -> movieRepository.findById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId)))
                .collect(Collectors.toSet());

        // Mută filmele
        sourceWatchlist.getMovies().removeAll(moviesToMove);
        targetWatchlist.getMovies().addAll(moviesToMove);

        // Salvează modificările
        watchlistRepository.save(sourceWatchlist);
        watchlistRepository.save(targetWatchlist);
    }

    public void removeMoviesFromWatchlist(Long watchlistId, List<Long> movieIds) {
        Watchlist watchlist = findWatchlistByIdOrThrow(watchlistId);

        // Verifică dacă utilizatorul are drepturi asupra watchlist-ului
        Long userId = getAuthenticatedUserId();
        if (!watchlist.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not authorized to modify this watchlist");
        }

        // Găsește filmele care trebuie șterse
        Set<Movie> moviesToRemove = movieIds.stream()
                .map(movieId -> movieRepository.findById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId)))
                .collect(Collectors.toSet());

        // Șterge filmele
        watchlist.getMovies().removeAll(moviesToRemove);

        // Salvează modificările
        watchlistRepository.save(watchlist);
    }
}
