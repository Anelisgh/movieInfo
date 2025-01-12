package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.UserRequest;
import com.example.movieInfo.dto.UserResponse;
import com.example.movieInfo.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest userRequest);
    UserResponse toResponse(User user);
}
