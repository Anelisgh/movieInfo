package com.example.movieInfo.mapper;

import com.example.movieInfo.dto.RegisterRequest;
import com.example.movieInfo.dto.UserResponse;
import com.example.movieInfo.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterRequest registerRequest);
    UserResponse toResponse(User user);
}
