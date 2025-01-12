package com.example.movieInfo.mapper;

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

    @Mapping(target = "movieTitles", expression = "java(mapMovieTitles(watchlist.getMovies()))")
    WatchlistResponse toResponseDTO(Watchlist watchlist);

    Watchlist toEntity(WatchlistRequest watchlistRequest);

    default Set<String> mapMovieTitles(Set<Movie> movies) {
        return movies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toSet());
    }
}
