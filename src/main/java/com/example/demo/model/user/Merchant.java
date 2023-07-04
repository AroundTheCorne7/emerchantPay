package com.example.demo.model.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "MERCHANT")
@NoArgsConstructor
@Builder
@Data
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "total_transaction_sum")
    private BigDecimal totalTransactionSum;


}
