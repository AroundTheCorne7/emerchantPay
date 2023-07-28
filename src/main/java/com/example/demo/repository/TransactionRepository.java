package com.example.demo.repository;

import com.example.demo.model.transactions.RefundTransaction;
import com.example.demo.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    void deleteByDateCreatedBefore(LocalTime expiryDate);

    Transaction findByUuid(String uuid);

    List<Transaction> findAllByReferenceUuid(String referenceUuid);
}
