package com.kodbank.service;

import com.kodbank.dto.BalanceResponse;
import com.kodbank.entity.KodUser;
import com.kodbank.repository.KodUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final KodUserRepository kodUserRepository;
    
    public BalanceResponse getBalance(String username) {
        Optional<KodUser> userOptional = kodUserRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        KodUser user = userOptional.get();
        return new BalanceResponse(user.getUsername(), user.getBalance());
    }
}
