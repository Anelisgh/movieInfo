package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.ReviewRequest;
import com.example.movieInfo.dto.ReviewResponse;
import com.example.movieInfo.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "username")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "reviewType", target = "reviewType")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "movie.id", source = "movieId")
    @Mapping(target = "reviewType", source = "reviewType")
    Review toReview(ReviewRequest reviewRequest);
}






