package com.example.demo.service;

import com.example.demo.model.user.dto.MerchantDto;

import java.util.List;

public interface MerchantService {
    List<MerchantDto> findAll(Long userId);

    MerchantDto updateMerchant(MerchantDto dto);

    void destroyMerchant(String uuid);

    MerchantDto findByUuid(String uuid);
}
