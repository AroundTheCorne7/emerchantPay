package com.example.demo.model.transactions;

import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

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
    private TransactionStatusEnum status;
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;
    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;
    @Column(name = "reference_uuid")
    private String referenceUuid;
    @Column(name = "date_created")
    private LocalTime dateCreated;
    @Column(name = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionStatus;
    @Column(name = "parent")
    private String parentTransaction;

}
