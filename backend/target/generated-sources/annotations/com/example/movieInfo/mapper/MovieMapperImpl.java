package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.model.Director;
import com.example.movieInfo.model.Movie;
import java.util.stream.Collectors;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-31T15:05:40+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public MovieResponse toResponse(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieResponse movieResponse = new MovieResponse();

        movieResponse.setId( movie.getId() );
        movieResponse.setDirectorName( movieDirectorName( movie ) );
        movieResponse.setPhotoUrl( movie.getPhotoUrl() );
        movieResponse.setTitle( movie.getTitle() );
        movieResponse.setDescription( movie.getDescription() );
        movieResponse.setReleaseYear( movie.getReleaseYear() );

        movieResponse.setGenre( movie.getGenre().name() );
        movieResponse.setActors( movie.getActors().stream().map(actor -> actor.getName()).collect(Collectors.toSet()) );

        return movieResponse;
    }

    @Override
    public Movie toEntity(MovieRequest movieRequest) {
        if ( movieRequest == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setDirector( map( movieRequest.getDirector() ) );
        movie.setTitle( movieRequest.getTitle() );
        movie.setDescription( movieRequest.getDescription() );
        movie.setReleaseYear( movieRequest.getReleaseYear() );
        movie.setGenre( movieRequest.getGenre() );
        movie.setPhotoUrl( movieRequest.getPhotoUrl() );

        movie.setActors( mapActors(movieRequest.getActors()) );

        return movie;
    }

    private String movieDirectorName(Movie movie) {
        if ( movie == null ) {
            return null;
        }
        Director director = movie.getDirector();
        if ( director == null ) {
            return null;
        }
        String name = director.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
