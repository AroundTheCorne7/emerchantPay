package com.example.demo.repository;

import com.example.demo.model.transactions.AuthorizeTransaction;
import com.example.demo.model.transactions.ChargeTransaction;
import com.example.demo.model.transactions.ReversalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizeTransactionRepository extends JpaRepository<AuthorizeTransaction, Long> {

    AuthorizeTransaction findByUuid(String uuid);
}
