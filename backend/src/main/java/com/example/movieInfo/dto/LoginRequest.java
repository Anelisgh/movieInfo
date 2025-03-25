package com.example.movieInfo.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Username cannot be empty")
    private String username; // Schimbat din 'name' în 'username'

    @NotBlank(message = "Password cannot be empty")
    private String password;
    private boolean rememberMe;
    public LoginRequest(){}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public @NotBlank(message = "Username cannot be empty") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username cannot be empty") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password cannot be empty") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password cannot be empty") String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}

