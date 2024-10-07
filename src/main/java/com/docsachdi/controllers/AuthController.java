package com.docsachdi.controllers;

import com.docsachdi.entities.User;
import com.docsachdi.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getUsername().isEmpty() ||
                    user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Username and password are required"));
            }

            User registeredUser = authService.registerUser(user.getEmail(), user.getUsername(), user.getPassword());

            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("success", true, "user", registeredUser));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "User already exists"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An error occurred during registration"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) {
        User user = authService.authenticateUser(username, password);
        return user != null ? ResponseEntity.status(HttpStatus.OK).body(user) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
