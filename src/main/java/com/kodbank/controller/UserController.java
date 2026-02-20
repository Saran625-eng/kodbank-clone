package com.kodbank.controller;

import com.kodbank.dto.BalanceResponse;
import com.kodbank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unauthorized access");
            return ResponseEntity.status(401).body(error);
        }
        
        try {
            String username = authentication.getName();
            BalanceResponse balanceResponse = userService.getBalance(username);
            return ResponseEntity.ok(balanceResponse);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
