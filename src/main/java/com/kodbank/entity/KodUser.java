package com.kodbank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "kod_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KodUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Long uid;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance = new BigDecimal("100000.00");
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "role", nullable = false, length = 20)
    private String role = "CUSTOMER";
    
    public KodUser(String username, String email, String password, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.balance = new BigDecimal("100000.00");
        this.role = "CUSTOMER";
    }
}
