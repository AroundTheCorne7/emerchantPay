package com.example.demo.model.transactions.dto;

import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransactionDto {

    private String uuid;
    private BigDecimal amount;
    private TransactionStatusEnum status;
    private TransactionTypeEnum transactionStatus;
    private String customerEmail;
    private String customerPhone;
    private String errorMessage;
    private String parentUuid;
}
