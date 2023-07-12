package com.example.demo.model.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MerchantDto {

    private String referenceUuid;
    private String name;
    private String description;
    private String email;
    private boolean isActive;
    private BigDecimal totalTransactionSum;
}
