package com.example.movieInfo.dto;


import jakarta.validation.constraints.*;

public class UserRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Email
    private String email;

    public UserRequest(){}

    public UserRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public @NotBlank @Size(max = 100) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 100) String name) {
        this.name = name;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }
}
