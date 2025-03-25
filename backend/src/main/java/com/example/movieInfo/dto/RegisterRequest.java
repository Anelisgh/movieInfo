package com.example.movieInfo.dto;


import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100)
    private String name;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    @NotBlank(message = "Confirm Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String confirmPassword;

    @AssertTrue(message = "Passwords must match")
    private boolean isPasswordsMatch() {
        return password != null && password.equals(confirmPassword);
    }

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}