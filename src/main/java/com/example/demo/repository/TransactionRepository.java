package com.example.demo.repository;

import com.example.demo.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    void deleteByDateCreatedBefore(LocalDate expiryDate);

    Transaction findByUuid(String uuid);

    List<Transaction> findAllByReferenceUuid(String referenceUuid);
}
