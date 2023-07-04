package com.example.demo.service.impl;

import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.service.TransactionService;

import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public TransactionDto createTransaction(RequestTransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setCustomerEmail(dto.getCustomerEmail());
        transaction.setCustomerPhone(dto.getCustomerPhone());
        transaction.setUuid(UUID.randomUUID().toString());
        transaction.setStatus(Transaction.TransactionStatus.AUTHORIZE);
    }
}
