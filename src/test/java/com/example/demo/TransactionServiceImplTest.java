package com.example.demo;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.TransactionRepository;
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

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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

        Transaction originalTransaction = new Transaction();
        originalTransaction.setUuid(transactionUUID);
        originalTransaction.setAmount(originalAmount);

        Merchant merchant = new Merchant();
        merchant.setTotalTransactionSum(originalAmount);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(merchant));
        when(transactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.refundTransaction(dto);

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

        Transaction originalTransaction = new Transaction();
        originalTransaction.setUuid(transactionUUID);
        originalTransaction.setAmount(originalAmount);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.empty());
        when(transactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.refundTransaction(dto);

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

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.empty());
        when(transactionRepository.findByUuid(transactionUUID)).thenReturn(null);

        // Test the service method
        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.refundTransaction(dto));

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

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(merchant));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.createTransaction(requestDto);

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
        TransactionDto transactionDto = transactionService.createTransaction(requestDto);

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

        Transaction originalTransaction = new Transaction();
        originalTransaction.setUuid(transactionUUID);
        originalTransaction.setAmount(originalAmount);

        Transaction parentTransaction = new Transaction();
        parentTransaction.setUuid(parentUUID);
        parentTransaction.setAmount(originalAmount);
        parentTransaction.setStatus(TransactionStatusEnum.APPROVED);

        Merchant merchant = new Merchant();
        merchant.setTotalTransactionSum(originalAmount);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.of(merchant));
        when(transactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);
        when(transactionRepository.findByUuid(parentUUID)).thenReturn(parentTransaction);

        // Test the service method
        TransactionDto transactionDto = transactionService.reverseTransaction(dto);

        // Verify the result
        assertEquals(TransactionStatusEnum.REVERSE, transactionDto.getStatus());
        assertEquals(TransactionTypeEnum.REVERSAL, parentTransaction.getTransactionStatus());
        assertEquals(originalAmount, merchant.getTotalTransactionSum());

        verify(transactionRepository, times(1)).save(parentTransaction);
        verify(transactionRepository, times(1)).save(originalTransaction);
        verify(merchantRepository, times(1)).save(merchant);
    }

    @Test
    public void testReverseTransaction_ParentTransactionNotFound() {
        // Mock data
        String referenceUuid = "merchant-reference-uuid";
        String transactionUUID = "transaction-uuid";
        String parentUUID = "parent-transaction-uuid";

        RequestTransactionDto dto = new RequestTransactionDto();
        dto.setReferenceUuid(referenceUuid);
        dto.setTransactionUUID(transactionUUID);
        dto.setParentUUID(parentUUID);

        Transaction originalTransaction = new Transaction();
        originalTransaction.setUuid(transactionUUID);

        // Mocking repository methods
        when(merchantRepository.findByReferenceUuid(referenceUuid)).thenReturn(Optional.empty());
        when(transactionRepository.findByUuid(transactionUUID)).thenReturn(originalTransaction);
        when(transactionRepository.findByUuid(parentUUID)).thenReturn(null); // Parent transaction not found

        // Test the service method
        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.reverseTransaction(dto));

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
        transaction1.setAmount(BigDecimal.valueOf(100));

        Transaction transaction2 = new Transaction();
        transaction2.setUuid("transaction-uuid-2");
        transaction2.setAmount(BigDecimal.valueOf(150));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        // Mocking repository methods
        when(transactionRepository.findAllByReferenceUuid(referenceUuid)).thenReturn(transactions);

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

        // Test the service method
        List<TransactionDto> transactionDtos = transactionService.getAllByMerchant(referenceUuid);

        // Verify the result
        assertEquals(0, transactionDtos.size());
    }

}
