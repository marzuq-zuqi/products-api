package com.example.product.controller;

import com.example.product.config.JwtUtil;
import com.example.product.dto.JwtRequest;
import com.example.product.dto.JwtResponse;
import com.example.product.model.AppUser;
import com.example.product.repository.UserRepository;
import com.example.product.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody JwtRequest request) {
        AppUser user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        return ResponseEntity.ok("User " + user.getUsername() + " registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            AppUser user = userRepository.findByUsername(jwtRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(user.getUsername(), user.getEmail(), user.getRole());

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException e) {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(errorBody);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@CookieValue(value = "auth_token", required = false) String token) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(401).body("Missing auth_token cookie");
            }

            String username = jwtUtil.extractUsername(token);

            AppUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole());

            return ResponseEntity.ok(userData);

        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", "Invalid token");
            return ResponseEntity.status(401).body(errorBody);
        }
    }
}