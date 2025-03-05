package com.example.MyGreetingApp.service;

import com.example.MyGreetingApp.DTOs.AuthUserDTO;
import com.example.MyGreetingApp.DTOs.LoginDTO;
import com.example.MyGreetingApp.model.AuthUser;
import com.example.MyGreetingApp.repository.AuthUserRepository;
import com.example.MyGreetingApp.Responses.ApiResponse;
import com.example.MyGreetingApp.Responses.LoginResponse;
import com.example.MyGreetingApp.security.JwtUtil;
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

        return new ResponseEntity<>(
                new LoginResponse("Login successful!", token),
                HttpStatus.OK);
    }

}