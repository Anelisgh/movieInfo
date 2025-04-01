package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.MovieUserDetails;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.model.Director;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.Review;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T16:15:37+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class MovieDetailsMapperImpl extends MovieDetailsMapper {

    @Override
    public MovieUserDetails toDetailsDTO(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieUserDetails movieUserDetails = new MovieUserDetails();

        movieUserDetails.setId( movie.getId() );
        movieUserDetails.setDirectorName( movieDirectorName( movie ) );
        movieUserDetails.setPhotoUrl( movie.getPhotoUrl() );
        movieUserDetails.setTitle( movie.getTitle() );
        movieUserDetails.setDescription( movie.getDescription() );
        movieUserDetails.setReleaseYear( movie.getReleaseYear() );
        movieUserDetails.setWatchlists( mapWatchlists( movie.getWatchlists() ) );
        movieUserDetails.setActors( mapActors( movie.getActors() ) );
        movieUserDetails.setReviews( reviewListToReviewResponseList( movie.getReviews() ) );

        movieUserDetails.setGenre( movie.getGenre().name() );

        return movieUserDetails;
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

    protected ReviewResponse reviewToReviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.setId( review.getId() );
        reviewResponse.setRating( review.getRating() );
        reviewResponse.setComment( review.getComment() );
        reviewResponse.setReviewType( review.getReviewType() );

        return reviewResponse;
    }

    protected List<ReviewResponse> reviewListToReviewResponseList(List<Review> list) {
        if ( list == null ) {
            return null;
        }

        List<ReviewResponse> list1 = new ArrayList<ReviewResponse>( list.size() );
        for ( Review review : list ) {
            list1.add( reviewToReviewResponse( review ) );
        }

        return list1;
    }
}
