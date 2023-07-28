package com.example.demo.controller;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<TransactionDto> createTransaction(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RequestTransactionDto dto) {
        return new ResponseEntity<>(service.manipulateTransaction(dto), HttpStatus.OK);
    }

}
