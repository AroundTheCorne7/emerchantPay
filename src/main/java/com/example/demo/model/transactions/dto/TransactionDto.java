package com.example.demo.model.transactions.dto;

import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String uuid;
    private BigDecimal amount;
    private TransactionStatusEnum status;
    private TransactionTypeEnum transactionStatus;
    @Email
    @NotBlank
    private String customerEmail;
    @NotBlank
    private String customerPhone;
    private String errorMessage;
    private String parentUuid;
}
