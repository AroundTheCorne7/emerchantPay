package com.example.demo.controller;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody RequestTransactionDto dto) {
        return new ResponseEntity<>(service.createTransaction(dto), HttpStatus.OK);
    }

    @PutMapping("/refund")
    public ResponseEntity<TransactionDto> refundTransaction(@RequestBody RequestTransactionDto dto) {
        return new ResponseEntity<>(service.refundTransaction(dto), HttpStatus.OK);
    }

    @PutMapping("/reverse")
    public ResponseEntity<TransactionDto> reverseTransaction(@RequestBody RequestTransactionDto dto) {
        return new ResponseEntity<>(service.reverseTransaction(dto), HttpStatus.OK);
    }
}
