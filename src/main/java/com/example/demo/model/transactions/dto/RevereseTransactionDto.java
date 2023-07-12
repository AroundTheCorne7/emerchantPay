package com.example.demo.model.transactions.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RevereseTransactionDto extends RequestTransactionDto{

    private String transactionUUID;
    private String parentUUID;
}
