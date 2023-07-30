package com.example.demo.repository;

import com.example.demo.model.transactions.ChargeTransaction;
import com.example.demo.model.transactions.RefundTransaction;
import com.example.demo.model.transactions.ReversalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeTransactionRepository extends JpaRepository<ChargeTransaction, Long> {

    ChargeTransaction findByUuid(String uuid);
}
