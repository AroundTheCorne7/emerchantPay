package com.example.demo.model.transactions.dto;

import com.example.demo.model.user.Merchant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RequestTransactionDto {

    private BigDecimal amount;
    private String customerEmail;
    private String customerPhone;
    private String referenceUuid;
    private BigDecimal customerAmount;

}
