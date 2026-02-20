package com.kodbank.service;

import com.kodbank.dto.*;
import com.kodbank.entity.KodUser;
import com.kodbank.entity.UserToken;
import com.kodbank.repository.KodUserRepository;
import com.kodbank.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final KodUserRepository kodUserRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (kodUserRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        if (kodUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create new user with default balance and CUSTOMER role
        KodUser user = new KodUser(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone()
        );
        
        kodUserRepository.save(user);
        
        return new AuthResponse("Registration successful", user.getUsername(), user.getRole());
    }
    
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Find user by username
        Optional<KodUser> userOptional = kodUserRepository.findByUsername(request.getUsername());
        
        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        KodUser user = userOptional.get();
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        
        // Delete old tokens for this user
        userTokenRepository.deleteByUid(user.getUid());
        
        // Store new token
        UserToken userToken = new UserToken(
                token,
                user.getUid(),
                LocalDateTime.now().plusDays(1)
        );
        userTokenRepository.save(userToken);
        
        return new AuthResponse("Login successful", user.getUsername(), user.getRole(), token);
    }
}
