package com.example.demo.controller;

import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionControllerTest {

    private TransactionService transactionService;

    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        transactionService = mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);
    }


    @Test
    public void testCreateTransaction() {
        // Arrange
        RequestTransactionDto requestDto = new RequestTransactionDto();
        TransactionDto mockTransactionDto = new TransactionDto();
        when(transactionService.manipulateTransaction(requestDto)).thenReturn(mockTransactionDto);

        // Act
        ResponseEntity<TransactionDto> responseEntity = transactionController.createTransaction(null, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockTransactionDto, responseEntity.getBody());
    }

    @Test
    public void testRefundTransaction() {
        // Arrange
        RequestTransactionDto requestDto = new RequestTransactionDto();
        requestDto.setStatus(TransactionStatusEnum.REFUNDED);
        TransactionDto mockTransactionDto = new TransactionDto();
        when(transactionService.manipulateTransaction(requestDto)).thenReturn(mockTransactionDto);

        // Act
        ResponseEntity<TransactionDto> responseEntity = transactionController.createTransaction(null, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockTransactionDto, responseEntity.getBody());
    }

    @Test
    public void testReverseTransaction() {
        // Arrange
        RequestTransactionDto requestDto = new RequestTransactionDto();
        TransactionDto mockTransactionDto = new TransactionDto();
        when(transactionService.manipulateTransaction(requestDto)).thenReturn(mockTransactionDto);

        // Act
        ResponseEntity<TransactionDto> responseEntity = transactionController.createTransaction(null, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockTransactionDto, responseEntity.getBody());
    }
}

