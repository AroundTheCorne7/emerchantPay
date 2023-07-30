package com.example.demo.controller;

import com.example.demo.model.transactions.dto.TransactionDto;
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
public class TransactionUIControllerTest {

    private TransactionService transactionService;

    private TransactionUIController transactionController;

    @BeforeEach
    public void setUp() {
        transactionService = mock(TransactionService.class);
        transactionController = new TransactionUIController(transactionService);
    }

    @Test
    public void testGetAllByMerchant() {
        // Arrange
        String referenceUuid = "merchant123";
        List<TransactionDto> mockTransactionList = Collections.singletonList(new TransactionDto());
        when(transactionService.getAllByMerchant(referenceUuid)).thenReturn(mockTransactionList);

        // Act
        ResponseEntity<List<TransactionDto>> responseEntity = transactionController.getAllByMerchant(referenceUuid);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockTransactionList, responseEntity.getBody());
    }
}
