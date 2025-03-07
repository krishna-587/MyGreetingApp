package com.example.MyGreetingApp.controller;

import com.example.MyGreetingApp.DTOs.AuthUserDTO;
import com.example.MyGreetingApp.DTOs.ForgotPasswordRequestDTO;
import com.example.MyGreetingApp.DTOs.LoginDTO;
import com.example.MyGreetingApp.DTOs.PasswordResetDTO;
import com.example.MyGreetingApp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API for user registration and login")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    // UC-9 : Registration
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with firstName, lastName, email, and password")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthUserDTO userDTO) {
        return authService.registerUser(userDTO);
    }

    // UC-10 : Login
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Login with email and password to get JWT token")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        return authService.loginUser(loginDTO);
    }

    // UC12 : Forgot Password
    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email, @RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        return authService.changePassword(email, forgotPasswordRequestDTO.getPassword());
    }

    // UC13 : Reset Password

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @RequestBody PasswordResetDTO passwordResetDTO){
        return authService.resetPassword(email , passwordResetDTO.getCurrentPassword(), passwordResetDTO.getNewPassword());
    }
}