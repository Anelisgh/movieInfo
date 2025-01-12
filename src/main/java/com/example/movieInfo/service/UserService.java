package com.example.movieInfo.service;

import com.example.movieInfo.dto.UserRequest;
import com.example.movieInfo.dto.UserResponse;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.UserMapper;
import com.example.movieInfo.model.User;
import com.example.movieInfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    private void checkForDuplicateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new DuplicateResourceException("User already exists with email: " + email);
        }
    }

    public UserResponse createUser(UserRequest userRequest) {
        checkForDuplicateEmail(userRequest.getEmail());
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = findUserByIdOrThrow(id);
        return userMapper.toResponse(user);
    }

    public User findById(Long id) {
        return findUserByIdOrThrow(id);
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User existingUser = findUserByIdOrThrow(id);

        if (existingUser.getEmail() != null && !existingUser.getEmail().equals(userRequest.getEmail())) {
            checkForDuplicateEmail(userRequest.getEmail());
        }

        existingUser.setName(userRequest.getName());
        existingUser.setEmail(userRequest.getEmail());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
    }
}
