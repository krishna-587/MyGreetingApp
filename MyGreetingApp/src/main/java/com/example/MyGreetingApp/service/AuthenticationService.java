package com.example.MyGreetingApp.service;

import com.example.MyGreetingApp.DTOs.AuthUserDTO;
import com.example.MyGreetingApp.DTOs.ForgotPasswordRequestDTO;
import com.example.MyGreetingApp.DTOs.LoginDTO;
import com.example.MyGreetingApp.model.AuthUser;
import com.example.MyGreetingApp.repository.AuthUserRepository;
import com.example.MyGreetingApp.Responses.ApiResponse;
import com.example.MyGreetingApp.Responses.LoginResponse;
import com.example.MyGreetingApp.security.JwtUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<?> registerUser(AuthUserDTO userDTO) {
        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Email is already in use."),
                    HttpStatus.BAD_REQUEST);
        }

        // Create new user
        AuthUser user = new AuthUser();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        // Send welcome email
        try {
            String emailBody = "<h1>Welcome to MyGreetingApp!</h1>"
                    + "<p>Dear " + user.getFirstName() + ",</p>"
                    + "<p>Thank you for registering with us.</p>"
                    + "<p>Best regards,<br>MyGreetingApp Team</p>";

            emailService.sendHtmlEmail(user.getEmail(), "Welcome to MyGreetingApp", emailBody);
        } catch (MessagingException e) {
            // Log error but continue - don't fail registration if email fails
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return new ResponseEntity<>(
                new ApiResponse(true, "User registered successfully!"),
                HttpStatus.CREATED);
    }

    public ResponseEntity<?> loginUser(LoginDTO loginDTO) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid email or password!"),
                    HttpStatus.UNAUTHORIZED);
        }

        // Generate JWT token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        // Send login notification
        try {
            emailService.sendSimpleEmail(
                    loginDTO.getEmail(),
                    "New Login to MyGreetingApp",
                    "Dear user, we detected a new login to your account. If this wasn't you, please contact support."
            );
        } catch (Exception e) {
            // Log error but continue - don't fail login if email fails
            System.err.println("Failed to send login notification email: " + e.getMessage());
        }

        return new ResponseEntity<>(
                new LoginResponse("Login successful!", token),
                HttpStatus.OK);
    }

    // forgot password
    public ResponseEntity<?> changePassword(String email, String newPassword) {
        Optional<AuthUser> temp = userRepository.findByEmail(email);

        if (temp.isPresent()) {
            AuthUser user = temp.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Send confirmation email
            try {
                emailService.sendSimpleEmail(
                        user.getEmail(),
                        "Password Changed Successfully",
                        "Dear " + user.getFirstName() + ",\n\nYour password has been successfully changed. If this wasn't you, please contact support immediately."
                );
            } catch (Exception e) {
                System.err.println("Failed to send password change confirmation email: " + e.getMessage());
            }

            return new ResponseEntity<>(
                    new ApiResponse(true, "Password changed successfully!"),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(false, "User not found!"),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Reset Password
    public ResponseEntity<?> resetPassword(String email, String currentPassword, String newPassword) {
        Optional<AuthUser> temp = userRepository.findByEmail(email);

        if (temp.isPresent()) {
            AuthUser user = temp.get();

            // Check if the current password matches
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Current password is incorrect!"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Send confirmation email
            try {
                emailService.sendSimpleEmail(
                        user.getEmail(),
                        "Password Reset Successful",
                        "Dear " + user.getFirstName() + ",\n\nYour password has been successfully reset. If this wasn't you, please contact support immediately."
                );
            } catch (Exception e) {
                System.err.println("Failed to send password reset confirmation email: " + e.getMessage());
            }

            return new ResponseEntity<>(
                    new ApiResponse(true, "Password reset successfully!"),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(false, "User not found!"),
                    HttpStatus.NOT_FOUND
            );
        }
    }

}