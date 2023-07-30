package com.example.demo.repository;

import com.example.demo.model.transactions.ReversalTransaction;
import com.example.demo.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface ReversalTransactionRepository extends JpaRepository<ReversalTransaction, Long> {


    ReversalTransaction findByUuid(String uuid);

}
