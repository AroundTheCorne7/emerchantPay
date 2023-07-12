package com.example.demo;

import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.RevereseTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.impl.TransactionServiceImpl;
import com.example.demo.util.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private InOrder inOrder;

    @BeforeEach
    public void setUp() {
        inOrder = inOrder(mapper, merchantRepository, transactionRepository);
    }

    @Test
    void createTransaction() {
        var dto = new RequestTransactionDto();
        var merchant = new Merchant();
        merchant.setActive(true);
        var transaction = new Transaction();

        when(merchantRepository.findByReferenceUuid(anyString())).thenReturn(Optional.of(merchant));
        when(mapper.convertDtoToEntity(dto)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(mapper.convertDtoToEntity(dto)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(merchantRepository.save(merchant)).thenReturn(merchant);

        TransactionDto actual = service.createTransaction(dto);
        assertNotNull(actual);

        inOrder.verify(merchantRepository).findByReferenceUuid(anyString());
        inOrder.verify(mapper).convertDtoToEntity(any(RequestTransactionDto.class));
        inOrder.verify(transactionRepository).save(any(Transaction.class));
        inOrder.verify(mapper).convertDtoToEntity(any(RequestTransactionDto.class));
        inOrder.verify(transactionRepository).save(any(Transaction.class));
        inOrder.verify(merchantRepository).save(any(Merchant.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void refundTransaction() {
        var transactionDto = new TransactionDto();
        var dto = new RevereseTransactionDto();
        var merchant = new Merchant();
        merchant.setActive(true);
        var transaction = new Transaction();

        when(merchantRepository.findByReferenceUuid(anyString())).thenReturn(Optional.of(merchant));
        when(transactionRepository.findByUuid(anyString())).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(mapper.convertEntityToDto(transaction)).thenReturn(transactionDto);

        TransactionDto actual = service.refundTransaction(dto);
        assertNotNull(actual);

        inOrder.verify(merchantRepository).findByReferenceUuid(anyString());
        inOrder.verify(transactionRepository).findByUuid(anyString());
        inOrder.verify(transactionRepository).save(any(Transaction.class));
        inOrder.verify(merchantRepository).save(any(Merchant.class));
        inOrder.verify(mapper).convertEntityToDto(any(Transaction.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void reverseTransaction() {
        var transactionDto = new TransactionDto();
        var dto = new RevereseTransactionDto();
        var merchant = new Merchant();
        merchant.setActive(true);
        var transaction = new Transaction();

        when(merchantRepository.findByReferenceUuid(anyString())).thenReturn(Optional.of(merchant));
        when(transactionRepository.findByUuid(anyString())).thenReturn(transaction);
        when(transactionRepository.findByUuid(anyString())).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(mapper.convertEntityToDto(transaction)).thenReturn(transactionDto);

        TransactionDto actual = service.refundTransaction(dto);
        assertNotNull(actual);

        inOrder.verify(merchantRepository).findByReferenceUuid(anyString());
        inOrder.verify(transactionRepository).findByUuid(anyString());
        inOrder.verify(transactionRepository).save(any(Transaction.class));
        inOrder.verify(transactionRepository).save(any(Transaction.class));
        inOrder.verify(merchantRepository).save(any(Merchant.class));
        inOrder.verify(mapper).convertEntityToDto(any(Transaction.class));
        inOrder.verifyNoMoreInteractions();
    }

}
