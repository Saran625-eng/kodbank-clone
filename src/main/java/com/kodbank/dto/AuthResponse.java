package com.kodbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    
    private String message;
    private String username;
    private String role;
    private String token;
    
    public AuthResponse(String message, String username, String role) {
        this.message = message;
        this.username = username;
        this.role = role;
    }
    
    public AuthResponse(String message, String username, String role, String token) {
        this.message = message;
        this.username = username;
        this.role = role;
        this.token = token;
    }
}
