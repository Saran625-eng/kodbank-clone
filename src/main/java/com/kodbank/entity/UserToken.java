package com.kodbank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid")
    private Long tid;
    
    @Column(name = "token", unique = true, nullable = false, length = 500)
    private String token;
    
    @Column(name = "uid", nullable = false)
    private Long uid;
    
    @Column(name = "expiry")
    private LocalDateTime expiry;
    
    public UserToken(String token, Long uid, LocalDateTime expiry) {
        this.token = token;
        this.uid = uid;
        this.expiry = expiry;
    }
}
