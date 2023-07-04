package com.example.demo.model.transactions.dto;

import com.example.demo.model.transactions.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransactionDto {

    private BigDecimal amount;
    private Transaction.TransactionStatus status;
    private String customerEmail;
    private String customerPhone;
}
