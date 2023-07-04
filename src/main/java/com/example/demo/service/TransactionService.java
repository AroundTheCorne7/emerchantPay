package com.example.demo.service;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    public TransactionDto createTransaction(RequestTransactionDto dto);
}
