package com.example.movieInfo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequest {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdatePasswordRequest(){}

    public @NotBlank String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank @Size(min = 8, message = "Password must be at least 8 characters long") String newPassword) {
        this.newPassword = newPassword;
    }
}
