package com.example.demo.util;

import com.example.demo.model.transactions.Transaction;
import com.example.demo.model.transactions.dto.TransactionDto;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {

    TransactionDto createDtoFromEntity(Transaction transaction);
}
