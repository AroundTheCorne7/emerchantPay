package com.example.demo.controller;

import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions-ui")
public class TransactionUIController {

    private final TransactionService service;

    @Autowired
    public TransactionUIController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/{referenceUuid}")
    public ResponseEntity<List<TransactionDto>> getAllByMerchant(@PathVariable String referenceUuid) {
        return new ResponseEntity<>(service.getAllByMerchant(referenceUuid), HttpStatus.OK);
    }
}
