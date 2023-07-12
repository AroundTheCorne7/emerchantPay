package com.example.demo.controller;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.RevereseTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/get-all/{referenceUuid}")
    public ResponseEntity<List<TransactionDto>> getAllByMerchant(@RequestParam String referenceUuid) {
        return new ResponseEntity(service.getAllByMerchant(referenceUuid), HttpStatus.OK);
    }

    @PostMapping("/create-transaction")
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody RequestTransactionDto dto) {
        return new ResponseEntity(service.createTransaction(dto), HttpStatus.OK);
    }

    @PostMapping("/refund-transaction")
    public ResponseEntity<TransactionDto> refundTransaction(@RequestBody RevereseTransactionDto dto) {
        return new ResponseEntity(service.refundTransaction(dto), HttpStatus.OK);
    }

    @PostMapping("/reverese-transaction")
    public ResponseEntity<TransactionDto> reverseTransaction(@RequestBody RevereseTransactionDto dto) {
        return new ResponseEntity(service.reverseTransaction(dto), HttpStatus.OK);
    }
}
