package com.example.demo.model.transactions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizeTransaction extends Transaction{
    @Min(1)
    @Max(Integer.MAX_VALUE)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

}
