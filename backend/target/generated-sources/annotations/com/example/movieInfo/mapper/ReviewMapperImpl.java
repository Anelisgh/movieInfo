package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.model.Movie;
import com.example.movieInfo.model.Review;
import com.example.movieInfo.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-31T14:35:08+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse toReviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.setUserId( reviewUserId( review ) );
        reviewResponse.setUsername( reviewUserName( review ) );
        reviewResponse.setMovieId( reviewMovieId( review ) );
        reviewResponse.setReviewType( review.getReviewType() );
        reviewResponse.setId( review.getId() );
        reviewResponse.setRating( review.getRating() );
        reviewResponse.setComment( review.getComment() );

        return reviewResponse;
    }

    @Override
    public Review toReview(ReviewRequest reviewRequest) {
        if ( reviewRequest == null ) {
            return null;
        }

        Review review = new Review();

        review.setUser( reviewRequestToUser( reviewRequest ) );
        review.setMovie( reviewRequestToMovie( reviewRequest ) );
        review.setReviewType( reviewRequest.getReviewType() );
        review.setRating( (double) reviewRequest.getRating() );
        review.setComment( reviewRequest.getComment() );

        return review;
    }

    private Long reviewUserId(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewUserName(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long reviewMovieId(Review review) {
        if ( review == null ) {
            return null;
        }
        Movie movie = review.getMovie();
        if ( movie == null ) {
            return null;
        }
        Long id = movie.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected User reviewRequestToUser(ReviewRequest reviewRequest) {
        if ( reviewRequest == null ) {
            return null;
        }

        User user = new User();

        user.setId( reviewRequest.getUserId() );

        return user;
    }

    protected Movie reviewRequestToMovie(ReviewRequest reviewRequest) {
        if ( reviewRequest == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setId( reviewRequest.getMovieId() );

        return movie;
    }
}
