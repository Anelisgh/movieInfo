package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.MovieDetails;
import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.Watchlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WatchlistMapper {
    WatchlistMapper INSTANCE = Mappers.getMapper(WatchlistMapper.class);

    @Mapping(target = "movies", expression = "java(mapMovieDetails(watchlist.getMovies()))")
    WatchlistResponse toResponseDTO(Watchlist watchlist);

    Watchlist toEntity(WatchlistRequest watchlistRequest);

    default Set<MovieDetails> mapMovieDetails(Set<Movie> movies) {
        return movies.stream()
                .map(movie -> new MovieDetails(
                        movie.getId(),       // Asigură-te că ID-ul este mapat
                        movie.getTitle(),   // title din Movie
                        movie.getPhotoUrl(), // photoUrl din Movie
                        movie.getReleaseYear() // releaseYear din Movie
                ))
                .collect(Collectors.toSet());
    }
}
