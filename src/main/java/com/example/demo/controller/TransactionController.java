package com.example.demo.controller;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PutMapping("/create-transaction")
    private ResponseEntity<TransactionDto> createTransaction(@RequestBody RequestTransactionDto dto) {
        return new ResponseEntity(service.createTransaction(dto), HttpStatus.OK);
    }
}
