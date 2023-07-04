package com.example.demo.model.transactions;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Table(name = "TRANSACTION")
@NoArgsConstructor
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "uuid", nullable = false)
    private String uuid;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "status", nullable = false)
    private TransactionStatus status;
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;
    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;
    @Column(name = "reference_id")
    private Long referenceId;

    public enum TransactionStatus {
        APPROVED, REVERSED, REFUNDED, ERROR;
    }
}
