package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.MovieRequest;
import com.example.movieInfo.dto.MovieResponse;
import com.example.movieInfo.model.Actor;
import com.example.movieInfo.model.Director;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.service.ActorService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring", imports = {Collectors.class})
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);
    @Mapping(target = "id", source = "id")
    @Mapping(source = "director.name", target = "directorName")
    @Mapping(target = "genre", expression = "java(movie.getGenre().name())")
    @Mapping(target = "actors", expression = "java(movie.getActors().stream().map(actor -> actor.getName()).collect(Collectors.toSet()))")
    @Mapping(source = "photoUrl", target = "photoUrl") // Adăugat
    MovieResponse toResponse(Movie movie);

    @Mapping(source = "director", target = "director")
    @Mapping(target = "actors", expression = "java(mapActors(movieRequest.getActors()))")
    Movie toEntity(MovieRequest movieRequest);

    default Director map(String directorName) {
        if (directorName == null) {
            return null;
        }
        Director director = new Director();
        director.setName(directorName);
        return director;
    }

    default Set<Actor> mapActors(Set<String> actorNames) {
        ActorService actorService = ActorService.getInstance();
        return actorNames.stream()
                .map(actorService::findByNameOrCreate)
                .collect(Collectors.toSet());
    }
}
