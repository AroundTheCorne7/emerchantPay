package com.example.demo.repository;

import com.example.demo.model.transactions.RefundTransaction;
import com.example.demo.model.transactions.ReversalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundTransactionRepository extends JpaRepository<RefundTransaction, Long> {

    RefundTransaction findByUuid(String uuid);
}
