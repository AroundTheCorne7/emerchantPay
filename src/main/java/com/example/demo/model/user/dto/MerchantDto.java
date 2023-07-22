package com.example.demo.model.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto {

    private String referenceUuid;
    @NotBlank
    private String name;
    private String description;
    @Email
    @NotBlank
    private String email;
    private boolean isActive;
    private BigDecimal totalTransactionSum;
}
