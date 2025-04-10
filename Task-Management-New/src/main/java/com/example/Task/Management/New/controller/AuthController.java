package com.example.Task.Management.New.controller;

import com.example.Task.Management.New.Repository.UserRepository;
import com.example.Task.Management.New.dto.AuthRequest;
import com.example.Task.Management.New.dto.AuthResponse;
import com.example.Task.Management.New.dto.RegisterRequest;
import com.example.Task.Management.New.model.JwtUtil;
import com.example.Task.Management.New.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }

        User user = new User(null, request.getUsername(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), request.getRoles());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password",null));
        }

        String token = jwtUtil.generateToken(request.getUsername());

        // Fetch user details
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new AuthResponse("User not found",user));
        }

        // Create a DTO to send user info + token
        AuthResponse response = new AuthResponse(token,user);

        return ResponseEntity.ok(response);
    }

}
