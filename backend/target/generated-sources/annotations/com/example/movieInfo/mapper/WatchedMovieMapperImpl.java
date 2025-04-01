package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.WatchedMovieRequest;
import com.example.movieInfo.model.WatchedMovies;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T16:15:37+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class WatchedMovieMapperImpl extends WatchedMovieMapper {

    @Override
    public WatchedMovies toEntity(WatchedMovieRequest watchedMovieRequest) {
        if ( watchedMovieRequest == null ) {
            return null;
        }

        WatchedMovies watchedMovies = new WatchedMovies();

        watchedMovies.setUser( mapUserIdToUser(watchedMovieRequest.getUserId()) );
        watchedMovies.setMovie( mapMovieIdToMovie(watchedMovieRequest.getMovieId()) );

        return watchedMovies;
    }
}
