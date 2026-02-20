package com.kodbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
    
    private String username;
    private BigDecimal balance;
    private String message;
    
    public BalanceResponse(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
        this.message = "Balance retrieved successfully";
    }
}
