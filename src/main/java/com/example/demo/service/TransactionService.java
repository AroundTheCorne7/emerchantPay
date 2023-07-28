package com.example.demo.service;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

//    TransactionDto createTransaction(RequestTransactionDto dto);
//
//    TransactionDto refundTransaction(RequestTransactionDto dto);
//
//    TransactionDto reverseTransaction(RequestTransactionDto dto);

    TransactionDto manipulateTransaction(RequestTransactionDto dto);

    List<TransactionDto> getAllByMerchant(String referenceUuid);
}
