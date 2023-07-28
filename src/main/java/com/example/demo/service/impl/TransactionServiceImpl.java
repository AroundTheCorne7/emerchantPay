package com.example.demo.service.impl;

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
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import com.example.demo.util.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper mapper;
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;


    @Autowired
    public TransactionServiceImpl(TransactionMapper mapper, MerchantRepository merchantRepository, TransactionRepository transactionRepository) {
        this.mapper = mapper;
        this.merchantRepository = merchantRepository;
        this.transactionRepository = transactionRepository;
    }

    private TransactionDto createTransaction(RequestTransactionDto dto) {
        Merchant merchant = merchantRepository.findByReferenceUuid(dto.getReferenceUuid()).orElseThrow(() -> new NotFoundException("Merchant with reference UUID not found: " + dto.getReferenceUuid()));
        if(!merchant.isActive()) {
            return createTransactionDto(new TransactionDto(), "Merchant not active");
        }
        Transaction transaction = mapper.convertDtoToEntity(dto);
        transaction.setUuid(UUID.randomUUID().toString());
        transaction.setTransactionStatus(TransactionTypeEnum.AUTHORIZE);
        transactionRepository.save(transaction);
        if(dto.getCustomerAmount().intValue() < dto.getAmount().intValue()) {
            return createTransactionDto(mapper.convertEntityToDto(transaction), "Insufficient Amount!");
        }
        Transaction chargeTransaction = mapper.convertDtoToEntity(dto);
        chargeTransaction.setUuid(UUID.randomUUID().toString());
        chargeTransaction.setStatus(TransactionStatusEnum.APPROVED);
        chargeTransaction.setTransactionStatus(TransactionTypeEnum.CHARGE);
        chargeTransaction.setParentTransaction(transaction.getUuid());
        merchant.setTotalTransactionSum(merchant.getTotalTransactionSum().add(dto.getAmount()));
        transactionRepository.save(chargeTransaction);
        merchantRepository.save(merchant);
        return mapper.convertEntityToDto(chargeTransaction);
    }

    private TransactionDto refundTransaction(RequestTransactionDto dto) {
        Merchant merchant = merchantRepository.findByReferenceUuid(dto.getReferenceUuid()).orElseThrow(() -> new NotFoundException("Merchant with reference UUID not found: " + dto.getReferenceUuid()));
        RefundTransaction transaction = (RefundTransaction) transactionRepository.findByUuid(dto.getTransactionUUID());
        if (transaction == null) {
            throw new NotFoundException("Transaction not found");
        }
        if(dto.getAmount().intValue() > transaction.getAmount().intValue()) {
            return createTransactionDto(new TransactionDto(), "You cant refund higher amount than the transaction value");
        }
        transaction.setTransactionStatus(TransactionTypeEnum.REFUND);
        transaction.setAmount(transaction.getAmount().subtract(dto.getAmount()));
        merchant.setTotalTransactionSum(merchant.getTotalTransactionSum().subtract(dto.getAmount()));
        transactionRepository.save(transaction);
        merchantRepository.save(merchant);
        return mapper.convertEntityToDto(transaction);
    }

    private TransactionDto reverseTransaction(RequestTransactionDto dto) {
        ReversalTransaction transaction = (ReversalTransaction) transactionRepository.findByUuid(dto.getTransactionUUID());
        if (transaction == null) {
            throw new NotFoundException("Transaction not found");
        }
        transaction.setStatus(TransactionStatusEnum.REVERSE);
        transaction.setTransactionStatus(TransactionTypeEnum.REVERSAL);
        transaction.setInvalid(true);
        transactionRepository.save(transaction);
        return mapper.convertEntityToDto(transaction);
    }

    @Override
    public TransactionDto manipulateTransaction(RequestTransactionDto dto) {
        if(dto.getStatus() == null) {
            return createTransaction(dto);
        }
        switch (dto.getStatus()) {
            case REVERSE -> {
                return reverseTransaction(dto);
            }
            case REFUNDED -> {
                return refundTransaction(dto);
            }
            default -> {
                return createTransaction(dto);
            }
        }
    }

    @Override
    public List<TransactionDto> getAllByMerchant(String referenceUuid) {
        List<Transaction> transactions = transactionRepository.findAllByReferenceUuid(referenceUuid);
        return mapper.convertEntitiesToDtos(transactions);
    }

    private static TransactionDto createTransactionDto(TransactionDto transactionDto, String errorMessage) {
        transactionDto.setStatus(TransactionStatusEnum.ERROR);
        transactionDto.setErrorMessage(errorMessage);
        return transactionDto;
    }
}
