package com.example.movieInfo.dto;

public class UserResponse {
    private String name;
    private String email;

    public UserResponse(){}

    public UserResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserResponse(Long id, String name, String email) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
