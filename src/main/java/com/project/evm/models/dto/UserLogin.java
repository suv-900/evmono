package com.project.evm.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLogin {
    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
