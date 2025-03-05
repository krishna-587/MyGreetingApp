package com.example.MyGreetingApp.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthUserDTO {

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First name must start with uppercase and contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last name must start with uppercase and contain only letters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@#$%^&*()\\-+=])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must contain at least one uppercase letter, one special character, one number, and be at least 8 characters long")
    private String password;
}