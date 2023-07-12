package com.example.demo.util;

import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.RequestTransactionDto;
import com.example.demo.model.transactions.dto.TransactionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    TransactionDto convertEntityToDto(Transaction transaction);
    Transaction convertDtoToEntity(RequestTransactionDto dto);

    List<TransactionDto> convertEntitiesToDtos(List<Transaction> transactions);
}
