package com.example.movieInfo.controller;

import com.example.movieInfo.dto.*;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.InvalidPasswordException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.UserMapper;
import com.example.movieInfo.model.User;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", response
            ));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (isAuthenticated) {
            // Generează token JWT
            String token = userService.generateJwtToken(loginRequest.getUsername());
            return ResponseEntity.ok().body(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        try {
            String oldUsername = authentication.getName();
            UserResponse updatedUser = userService.updateProfile(oldUsername, request);
            // Verifică dacă username-ul s-a schimbat
            boolean usernameChanged = !oldUsername.equals(updatedUser.getName());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", updatedUser,
                    "usernameChanged", usernameChanged // Trimite un flag către frontend
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            userService.updatePassword(username, request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Password updated successfully"
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            UserResponse userResponse = userMapper.toResponse(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", userResponse
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // Handler pentru DuplicateResourceException
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}