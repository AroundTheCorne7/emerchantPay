package com.example.demo.service;

import com.example.demo.model.user.dto.MerchantDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MerchantService {
    List<MerchantDto> findAll();

    MerchantDto updateMerchant(MerchantDto dto);

    void destroyMerchant(String uuid);
}
