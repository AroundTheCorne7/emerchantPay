package com.example.demo.model.transactions.dto;

import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import com.example.demo.model.user.Merchant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RequestTransactionDto {

    private BigDecimal amount;
    @Email
    @NotBlank
    private String customerEmail;
    @NotBlank
    private String customerPhone;
    private String referenceUuid;
    private BigDecimal customerAmount;
    private String transactionUUID;
    private String parentUUID;
    @NotBlank
    private TransactionStatusEnum status;

}
