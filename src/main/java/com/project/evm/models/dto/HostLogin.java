package com.project.evm.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HostLogin {
    @NotBlank(message="username cannot be blank")
    private String username;
    
    @NotBlank(message="password cannot be blank")
    private String password; 
}
