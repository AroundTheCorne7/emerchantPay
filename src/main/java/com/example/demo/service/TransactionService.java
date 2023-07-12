package com.example.demo.service;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.RevereseTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TransactionService {

    TransactionDto createTransaction(RequestTransactionDto dto);

    TransactionDto refundTransaction(RevereseTransactionDto dto);

    TransactionDto reverseTransaction(RevereseTransactionDto dto);

    List<TransactionDto> getAllByMerchant(String referenceUuid);
}
