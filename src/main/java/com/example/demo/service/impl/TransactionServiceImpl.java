package com.example.demo.service.impl;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.RevereseTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import com.example.demo.model.transactions.enums.TransactionStatusEnum;
import com.example.demo.model.transactions.enums.TransactionTypeEnum;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import com.example.demo.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionMapper mapper;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionDto createTransaction(RequestTransactionDto dto) {
        Merchant merchant = merchantRepository.findByReferenceUuid(dto.getReferenceUuid()).orElseThrow(NotFoundException::new);
        if(!merchant.isActive()) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setStatus(TransactionStatusEnum.ERROR);
            transactionDto.setErrorMessage("Merchant not active");
            return transactionDto;
        }
        Transaction transaction = mapper.convertDtoToEntity(dto);
        transaction.setUuid(UUID.randomUUID().toString());
        transaction.setTransactionStatus(TransactionTypeEnum.AUTHORIZE);
        transactionRepository.save(transaction);
        if(dto.getCustomerAmount().intValue() < dto.getAmount().intValue()) {
            TransactionDto transactionDto = mapper.convertEntityToDto(transaction);
            transactionDto.setStatus(TransactionStatusEnum.ERROR);
            transactionDto.setErrorMessage("Insufficient Amount!");
            return transactionDto;
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

    @Override
    public TransactionDto refundTransaction(RevereseTransactionDto dto) {
        Merchant merchant = merchantRepository.findByReferenceUuid(dto.getReferenceUuid()).orElseThrow(NotFoundException::new);
        Transaction transaction = transactionRepository.findByUuid(dto.getTransactionUUID());
        if (transaction == null) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setStatus(TransactionStatusEnum.ERROR);
            transactionDto.setErrorMessage("Transaction not found");
            return transactionDto;
        }
        if(dto.getAmount().intValue() > transaction.getAmount().intValue()) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setStatus(TransactionStatusEnum.ERROR);
            transactionDto.setErrorMessage("You cant refund higher amount than the transaction value");
            return transactionDto;
        }
        transaction.setTransactionStatus(TransactionTypeEnum.REFUND);
        transaction.setAmount(transaction.getAmount().subtract(dto.getAmount()));
        merchant.setTotalTransactionSum(merchant.getTotalTransactionSum().subtract(dto.getAmount()));
        transactionRepository.save(transaction);
        merchantRepository.save(merchant);
        return mapper.convertEntityToDto(transaction);
    }

    @Override
    public TransactionDto reverseTransaction(RevereseTransactionDto dto) {
        Merchant merchant = merchantRepository.findByReferenceUuid(dto.getReferenceUuid()).orElseThrow(NotFoundException::new);
        Transaction transaction = transactionRepository.findByUuid(dto.getTransactionUUID());
        Transaction parentTransaction = transactionRepository.findByUuid(dto.getParentUUID());
        if (transaction == null || parentTransaction == null) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setStatus(TransactionStatusEnum.ERROR);
            transactionDto.setErrorMessage("Transaction not found");
            return transactionDto;
        }
        parentTransaction.setStatus(TransactionStatusEnum.REVERSE);
        parentTransaction.setTransactionStatus(TransactionTypeEnum.REVERSAL);
        merchant.setTotalTransactionSum(merchant.getTotalTransactionSum().subtract(parentTransaction.getAmount()));
        transactionRepository.save(transaction);
        merchantRepository.save(merchant);
        return mapper.convertEntityToDto(parentTransaction);
    }

    @Override
    public List<TransactionDto> getAllByMerchant(String referenceUuid) {
        List<Transaction> transactions = transactionRepository.findAllByReferenceUuid(referenceUuid);
        return mapper.convertEntitiesToDtos(transactions);
    }
}
