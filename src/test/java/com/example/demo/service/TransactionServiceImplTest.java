package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.transactions.RefundTransaction;
import com.example.demo.model.transactions.ReversalTransaction;
import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.RefundTransactionRepository;
import com.example.demo.repository.ReversalTransactionRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import com.example.demo.service.impl.TransactionServiceImpl;
import com.example.demo.util.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceImplTest {

    private TransactionService transactionService;
    private TransactionMapper mapper;
    private MerchantRepository merchantRepository;
    private TransactionRepository transactionRepository;
    private ReversalTransactionRepository reversalTransactionRepository;
    private RefundTransactionRepository refundTransactionRepository;

    @BeforeEach
    public void setUp() {
        mapper = mock(TransactionMapper.class);
        merchantRepository = mock(MerchantRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        reversalTransactionRepository = mock(ReversalTransactionRepository.class);
        refundTransactionRepository = mock(RefundTransactionRepository.class);
        transactionService = new TransactionServiceImpl(mapper, merchantRepository, transactionRepository, reversalTransactionRepository, refundTransactionRepository);
    }

    @Test
    public void testRefundTransaction_SuccessfulRefund() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUUID = "transaction-uuid";
        BigDecimal originalAmount = BigDecimal.valueOf(100);
        BigDecimal refundAmount = BigDecimal.valueOf(50);

        RequestTransactionDto dto = new RequestTransactionDto();
        dto.setReferenceUuid(referenceUuid);
        dto.setTransactionUUID(transactionUUID);
        dto.setAmount(refundAmount);
        dto.setStatus(TransactionStatusEnum.REFUNDED);

        RefundTransaction originalTransaction = new RefundTransaction();
        originalTransaction.setUuid(transactionUUID);
        originalTransaction.setAmount(originalAmount);

        Merchant merchant = new Merchant();
        merchant.setTotalTransactionSum(originalAmount);

        TransactionDto actual = new TransactionDto();
        actual.setStatus(TransactionStatusEnum.REFUNDED);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(merchant));
        when(refundTransactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);
        when(mapper.convertEntityToDto(any(Transaction.class))).thenReturn(actual);

        // Test the service method
        TransactionDto transactionDto = transactionService.manipulateTransaction(dto);

        // Verify the result
        assertEquals(TransactionStatusEnum.REFUNDED, transactionDto.getStatus());
        assertEquals(originalAmount.subtract(refundAmount), originalTransaction.getAmount());
        assertEquals(originalAmount.subtract(refundAmount), merchant.getTotalTransactionSum());

        verify(transactionRepository, times(1)).save(originalTransaction);
        verify(merchantRepository, times(1)).save(merchant);
    }

    @Test
    public void testRefundTransaction_RefundAmountGreaterThanTransactionAmount() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUUID = "transaction-uuid";
        BigDecimal originalAmount = BigDecimal.valueOf(100);
        BigDecimal refundAmount = BigDecimal.valueOf(150);

        RequestTransactionDto dto = new RequestTransactionDto();
        dto.setReferenceUuid(referenceUuid);
        dto.setTransactionUUID(transactionUUID);
        dto.setAmount(refundAmount);
        dto.setStatus(TransactionStatusEnum.REFUNDED);

        RefundTransaction originalTransaction = new RefundTransaction();
        originalTransaction.setUuid(transactionUUID);
        originalTransaction.setAmount(originalAmount);

        Merchant merchant = new Merchant();
        merchant.setReferenceUuid(referenceUuid);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(anyString())).thenReturn(Optional.of(merchant));
        when(refundTransactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.manipulateTransaction(dto);

        // Verify the result
        assertEquals(TransactionStatusEnum.ERROR, transactionDto.getStatus());
        assertEquals("You cant refund higher amount than the transaction value", transactionDto.getErrorMessage());

        // Verify that no changes were made to the transaction and merchant
        verify(transactionRepository, never()).save(originalTransaction);
        verify(merchantRepository, never()).save(any(Merchant.class));
    }

    @Test
    public void testRefundTransaction_TransactionNotFound() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUUID = "transaction-uuid";
        BigDecimal refundAmount = BigDecimal.valueOf(50);

        RequestTransactionDto dto = new RequestTransactionDto();
        dto.setReferenceUuid(referenceUuid);
        dto.setTransactionUUID(transactionUUID);
        dto.setAmount(refundAmount);
        dto.setStatus(TransactionStatusEnum.REFUNDED);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(new Merchant()));
        when(refundTransactionRepository.findByUuid(transactionUUID)).thenReturn(null);

        // Test the service method
        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.manipulateTransaction(dto));

        // Verify the exception
        assertEquals("Transaction not found", exception.getMessage());

        // Verify that no changes were made to the transaction and merchant
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(merchantRepository, never()).save(any(Merchant.class));
    }

    @Test
    public void testCreateTransaction() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUuid = "transaction-uuid";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal customerAmount = BigDecimal.valueOf(120);

        RequestTransactionDto requestDto = new RequestTransactionDto();
        requestDto.setReferenceUuid(referenceUuid);
        requestDto.setAmount(amount);
        requestDto.setCustomerAmount(customerAmount);

        Merchant merchant = new Merchant();
        merchant.setActive(true);
        merchant.setTotalTransactionSum(BigDecimal.ZERO);

        Transaction transaction = new Transaction();
        transaction.setUuid(transactionUuid);
        TransactionDto dto = new TransactionDto();
        dto.setStatus(TransactionStatusEnum.APPROVED);
        dto.setUuid(transactionUuid);

        // Mocking repository methods
        when(mapper.convertDtoToEntity(any(RequestTransactionDto.class))).thenReturn(transaction);
        when(mapper.convertEntityToDto(any(Transaction.class))).thenReturn(dto);
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(merchant));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.manipulateTransaction(requestDto);

        // Verify the result
        assertEquals(TransactionStatusEnum.APPROVED, transactionDto.getStatus());
        assertEquals(transactionUuid, transactionDto.getUuid());

        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(merchantRepository, times(1)).save(merchant);
    }

    @Test
    public void testCreateTransaction_MerchantNotActive() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal customerAmount = BigDecimal.valueOf(120);

        RequestTransactionDto requestDto = new RequestTransactionDto();
        requestDto.setReferenceUuid(referenceUuid);
        requestDto.setAmount(amount);
        requestDto.setCustomerAmount(customerAmount);

        Merchant merchant = new Merchant();
        merchant.setActive(false);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(merchant));

        // Test the service method
        TransactionDto transactionDto = transactionService.manipulateTransaction(requestDto);

        // Verify the result
        assertEquals(TransactionStatusEnum.ERROR, transactionDto.getStatus());
        assertEquals("Merchant not active", transactionDto.getErrorMessage());

        verify(transactionRepository, times(0)).save(any(Transaction.class));
        verify(merchantRepository, times(0)).save(any(Merchant.class));
    }

    @Test
    public void testReverseTransaction_SuccessfulReversal() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUUID = "transaction-uuid";
        String parentUUID = "parent-transaction-uuid";
        BigDecimal originalAmount = BigDecimal.valueOf(100);

        RequestTransactionDto dto = new RequestTransactionDto();
        dto.setReferenceUuid(referenceUuid);
        dto.setTransactionUUID(transactionUUID);
        dto.setParentUUID(parentUUID);
        dto.setStatus(TransactionStatusEnum.REVERSE);

        ReversalTransaction originalTransaction = new ReversalTransaction();
        originalTransaction.setUuid(transactionUUID);

        TransactionDto transactionData = new TransactionDto();
        transactionData.setStatus(TransactionStatusEnum.REVERSE);
        transactionData.setTransactionStatus(TransactionTypeEnum.REVERSAL);

        // Mocking repository methods
        when(mapper.convertEntityToDto(any(Transaction.class))).thenReturn(transactionData);
        when(reversalTransactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.manipulateTransaction(dto);

        // Verify the result
        assertEquals(TransactionStatusEnum.REVERSE, transactionDto.getStatus());
        assertEquals(TransactionTypeEnum.REVERSAL, transactionDto.getTransactionStatus());

        verify(transactionRepository, times(1)).save(originalTransaction);
    }

    @Test
    public void testReverseTransaction_TransactionNotFound() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUUID = "transaction-uuid";
        String parentUUID = "parent-transaction-uuid";

        RequestTransactionDto dto = new RequestTransactionDto();
        dto.setReferenceUuid(referenceUuid);
        dto.setTransactionUUID(transactionUUID);
        dto.setParentUUID(parentUUID);
        dto.setStatus(TransactionStatusEnum.REVERSE);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.empty());
        when(reversalTransactionRepository.findByUuid(transactionUUID)).thenReturn(null);

        // Test the service method
        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.manipulateTransaction(dto));

        // Verify the exception
        assertEquals("Transaction not found", exception.getMessage());

        // Verify that no changes were made to the transactions and merchant
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(merchantRepository, never()).save(any(Merchant.class));
    }

    @Test
    public void testGetAllByMerchant_TransactionsFound() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";

        Transaction transaction1 = new Transaction();
        transaction1.setUuid("transaction-uuid-1");

        Transaction transaction2 = new Transaction();
        transaction2.setUuid("transaction-uuid-2");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        List<TransactionDto> dtos = List.of(new TransactionDto(), new TransactionDto());

        // Mocking repository methods
        when(transactionRepository.findAllByReferenceUuid(referenceUuid)).thenReturn(transactions);
        when(mapper.convertEntitiesToDtos(transactions)).thenReturn(dtos);

        // Test the service method
        List<TransactionDto> transactionDtos = transactionService.getAllByMerchant(referenceUuid);

        // Verify the result
        assertEquals(2, transactionDtos.size());
        // Add more assertions for the individual transactions if needed.
    }

    @Test
    public void testGetAllByMerchant_NoTransactionsFound() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";

        // Mocking repository methods
        when(transactionRepository.findAllByReferenceUuid(referenceUuid)).thenReturn(new ArrayList<>());
        when(mapper.convertEntitiesToDtos(List.of())).thenReturn(List.of());

        // Test the service method
        List<TransactionDto> transactionDtos = transactionService.getAllByMerchant(referenceUuid);

        // Verify the result
        assertEquals(0, transactionDtos.size());
    }

}
