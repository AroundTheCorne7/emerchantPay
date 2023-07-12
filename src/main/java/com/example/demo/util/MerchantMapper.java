package com.example.demo.util;

import com.example.demo.model.user.Merchant;
import com.example.demo.model.user.dto.MerchantDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper {

    List<MerchantDto> convertEntitiesToDtos(List<Merchant> merchants);

    MerchantDto convertEntityToDto(Merchant merchant);
}
