package com.example.movieInfo.service;

import com.example.movieInfo.dto.UserRequest;
import com.example.movieInfo.dto.UserResponse;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.UserMapper;
import com.example.movieInfo.model.User;
import com.example.movieInfo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userRequest = new UserRequest();
        userRequest.setName("John Smith");
        userRequest.setEmail("john.smith@example.com");

        user = new User();
        user.setId(1L);
        user.setName("John Smith");
        user.setEmail("john.smith@example.com");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        UserResponse expectedResponse = new UserResponse("John Smith", "john.smith@example.com");
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        UserResponse result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());

        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void testGetUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponse expectedResponse = new UserResponse("John Smith", "john.smith@example.com");
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);
        UserResponse result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }


    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserRequest updatedRequest = new UserRequest("John Smith Updated", "john.updated@example.com");
        UserResponse expectedResponse = new UserResponse("John Smith Updated", "john.updated@example.com");
        when(userRepository.save(any(User.class))).thenReturn(new User("John Smith Updated", "john.updated@example.com"));
        when(userMapper.toResponse(any(User.class))).thenReturn(expectedResponse);
        UserResponse result = userService.updateUser(1L, updatedRequest);

        assertNotNull(result);
        assertEquals("John Smith Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    public void testUpdateUser_EmailAlreadyExists() {
        UserRequest updatedRequest = new UserRequest();
        updatedRequest.setName("John Smith Updated");
        updatedRequest.setEmail("john.smith.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updatedRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(DuplicateResourceException.class, () -> userService.updateUser(1L, updatedRequest));
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(user);
    }


    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
