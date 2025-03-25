package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.model.*;
import com.example.movieInfo.repository.WatchedMovieRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public abstract class MovieDetailsMapper {
    @Autowired
    private WatchedMovieRepository watchedMovieRepository;

    @Mapping(target = "id", source = "id")
    @Mapping(target = "directorName", source = "director.name")
    @Mapping(target = "genre", expression = "java(movie.getGenre().name())")
    @Mapping(target = "photoUrl", source = "photoUrl") // Adăugat
    public abstract MovieUserDetails toDetailsDTO(Movie movie);

    public Set<String> mapWatchlists(Set<Watchlist> watchlists) {
        return watchlists.stream()
                .map(Watchlist::getName)
                .collect(Collectors.toSet());
    }

    public List<String> mapReviews(List<Review> reviews) {
        return reviews.stream()
                .map(Review::getComment)
                .collect(Collectors.toList());
    }

    public Set<String> mapActors(Set<Actor> actors) {
        return actors.stream()
                .map(Actor::getName)
                .collect(Collectors.toSet());
    }
}

