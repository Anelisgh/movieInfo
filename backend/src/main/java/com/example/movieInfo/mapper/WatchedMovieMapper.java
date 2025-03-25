package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.User;
import com.example.movieInfo.model.WatchedMovies;
import com.example.movieInfo.service.MovieService;
import com.example.movieInfo.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class WatchedMovieMapper {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Mapping(target = "user", expression = "java(mapUserIdToUser(watchedMovieRequest.getUserId()))")
    @Mapping(target = "movie", expression = "java(mapMovieIdToMovie(watchedMovieRequest.getMovieId()))")
    public abstract WatchedMovies toEntity(WatchedMovieRequest watchedMovieRequest);

    protected User mapUserIdToUser(Long userId) {
        return userService.findById(userId);
    }

    protected Movie mapMovieIdToMovie(Long movieId) {
        return movieService.findById(movieId);
    }
}
