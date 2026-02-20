package com.kodbank.controller;

import com.kodbank.dto.*;
import com.kodbank.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request);
            
            // Add token to cookie (not HttpOnly for JavaScript access)
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", authResponse.getToken());
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            cookie.setMaxAge(86400); // 24 hours
            cookie.setSecure(false); // Set to true in production with HTTPS
            response.addCookie(cookie);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", authResponse.getMessage());
            result.put("username", authResponse.getUsername());
            result.put("role", authResponse.getRole());
            result.put("token", authResponse.getToken());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/login")
    public void loginPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login.html");
    }
    
    @GetMapping("/register")
    public void registerPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/register.html");
    }
}
