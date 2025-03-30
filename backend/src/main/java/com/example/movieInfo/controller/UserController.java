package com.example.movieInfo.controller;

import com.example.movieInfo.dto.*;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.InvalidPasswordException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.exception.UsernameNotFoundException;
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
import org.springframework.security.authentication.BadCredentialsException;
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
        try {
            userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            String token = userService.generateJwtToken(loginRequest.getUsername());
            return ResponseEntity.ok(Map.of("success", true, "token", token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "error", "Username not found"
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "error", "Invalid password"
            ));
        }
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
            // Verifică autentificarea
            if (authentication == null) {
                System.out.println("⚠️ /auth/me called without authentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
            }

            String username = authentication.getName();
            System.out.println("🔑 Authenticated user: " + username);

            // Log database query
            System.out.println("🔍 Searching user in DB: " + username);
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> {
                        System.out.println("❌ User not found: " + username);
                        return new ResourceNotFoundException("User not found");
                    });

            System.out.println("✅ Found user: " + user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", userMapper.toResponse(user)
            ));
        } catch (Exception e) {
            System.err.println("🔥 Error in /auth/me: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "error", "Internal server error"
            ));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            userService.deleteUser(user.getId());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Account deleted successfully"
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
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