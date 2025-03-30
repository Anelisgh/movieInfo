package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.WatchlistRequest;
import com.example.movieInfo.dto.WatchlistResponse;
import com.example.movieInfo.model.Watchlist;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-30T19:29:26+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class WatchlistMapperImpl implements WatchlistMapper {

    @Override
    public WatchlistResponse toResponseDTO(Watchlist watchlist) {
        if ( watchlist == null ) {
            return null;
        }

        WatchlistResponse watchlistResponse = new WatchlistResponse();

        watchlistResponse.setId( watchlist.getId() );
        watchlistResponse.setName( watchlist.getName() );

        watchlistResponse.setMovies( mapMovieDetails(watchlist.getMovies()) );

        return watchlistResponse;
    }

    @Override
    public Watchlist toEntity(WatchlistRequest watchlistRequest) {
        if ( watchlistRequest == null ) {
            return null;
        }

        Watchlist watchlist = new Watchlist();

        watchlist.setId( watchlistRequest.getId() );
        watchlist.setName( watchlistRequest.getName() );

        return watchlist;
    }
}
