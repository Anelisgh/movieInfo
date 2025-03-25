package com.example.movieInfo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;


    public UpdateProfileRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UpdateProfileRequest(){}

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }
}
