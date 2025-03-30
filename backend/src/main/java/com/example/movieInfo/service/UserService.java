package com.example.movieInfo.service;

import com.example.movieInfo.dto.RegisterRequest;
import com.example.movieInfo.dto.UpdatePasswordRequest;
import com.example.movieInfo.dto.UpdateProfileRequest;
import com.example.movieInfo.dto.UserResponse;
import com.example.movieInfo.exception.DuplicateResourceException;
import com.example.movieInfo.exception.InvalidPasswordException;
import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.mapper.UserMapper;
import com.example.movieInfo.model.User;
import com.example.movieInfo.config.JwtConfig;
import com.example.movieInfo.repository.ReviewRepository;
import com.example.movieInfo.repository.UserRepository;
import com.example.movieInfo.repository.WatchedMovieRepository;
import com.example.movieInfo.repository.WatchlistRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final WatchedMovieRepository watchedMovieRepository;
    private final ReviewRepository reviewRepository;
    private final WatchlistRepository watchlistRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, JwtConfig jwtConfig, WatchedMovieRepository watchedMovieRepository, ReviewRepository reviewRepository, WatchlistRepository watchlistRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtConfig = jwtConfig;
        this.watchedMovieRepository = watchedMovieRepository;
        this.reviewRepository = reviewRepository;
        this.watchlistRepository = watchlistRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (!userRepository.existsByName(name)) {
            throw new UsernameNotFoundException("Username not found");
        }

        return userRepository.findByName(name)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getName(),
                        user.getPassword(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new BadCredentialsException("Invalid password"));
    }

    public UserResponse registerUser(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new IllegalArgumentException("RegisterRequest cannot be null");
        }

        // Verificări pentru duplicate
        checkForDuplicateEmail(registerRequest.getEmail());
        checkForDuplicateName(registerRequest.getName());

        // Codificare parolă
        String encodedPassword = passwordEncoder.encode(
                Objects.requireNonNull(registerRequest.getPassword(), "Password cannot be null")
        );

        // Creare utilizator
        User user = userMapper.toEntity(registerRequest);
        user.setPassword(encodedPassword);

        // Salvare și returnare răspuns
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        return userMapper.toResponse(savedUser);
    }

    public void authenticateUser(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    private void checkForDuplicateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new DuplicateResourceException("An user already exists with this email");
        }
    }

    private void checkForDuplicateName(String name) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            throw new DuplicateResourceException("An user already exists with this name");
        }
    }

    public User findById(Long id) {
        return findUserByIdOrThrow(id);
    }

    @Transactional
    public UserResponse updateUser(Long id, RegisterRequest registerRequest) {
        User existingUser = findUserByIdOrThrow(id);

        if (existingUser.getEmail() != null && !existingUser.getEmail().equals(registerRequest.getEmail())) {
            checkForDuplicateEmail(registerRequest.getEmail());
        }

        existingUser.setName(registerRequest.getName());
        existingUser.setEmail(registerRequest.getEmail());

        // 🔹 Dacă registerRequest conține o nouă parolă, o criptăm și o actualizăm
        if (registerRequest.getPassword() != null && !registerRequest.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserByIdOrThrow(id);
        watchedMovieRepository.deleteAllByUser(user);
        reviewRepository.deleteAllByUser(user);
        watchlistRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 ore
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey().getBytes())
                .compact();
    }

    @Transactional
    public UserResponse updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            checkForDuplicateEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null && !request.getName().equals(user.getName())) {
            checkForDuplicateName(request.getName());
            user.setName(request.getName());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void updatePassword(String username, UpdatePasswordRequest request) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    }

    //    public UserResponse createUser(RegisterRequest registerRequest) {
//        checkForDuplicateEmail(registerRequest.getEmail());
//        User user = userMapper.toEntity(registerRequest);
//
//        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//
//        User savedUser = userRepository.save(user);
//        return userMapper.toResponse(savedUser);
//    }
//
//    public UserResponse getUserById(Long id) {
//        User user = findUserByIdOrThrow(id);
//        return new UserResponse(user.getName(), user.getEmail());
//    }
