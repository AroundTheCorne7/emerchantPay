package com.example.demo.service.impl;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.user.Merchant;
import com.example.demo.model.user.dto.MerchantDto;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.MerchantService;
import com.example.demo.util.MerchantMapper;
import com.example.demo.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository repository;

    private final MerchantMapper mapper;

    @Autowired
    public MerchantServiceImpl(MerchantMapper mapper, MerchantRepository merchantRepository) {
        this.mapper = mapper;
        this.repository = merchantRepository;
    }

    @Override
    public List<MerchantDto> findAll() {
        List<Merchant> merchants = repository.findAllByIsActiveTrue();
        return mapper.convertEntitiesToDtos(merchants);
    }

    @Override
    public MerchantDto updateMerchant(MerchantDto dto) {
        Merchant merchant = repository.findByReferenceUuid(dto.getReferenceUuid()).orElseThrow(() -> new NotFoundException("Merchant with reference UUID not found: " + dto.getReferenceUuid()));
        if(merchant != null) {
            merchant.setDescription(dto.getDescription());
            merchant.setName(dto.getName());
            merchant.setEmail(dto.getEmail());
            repository.save(merchant);
        }
        return mapper.convertEntityToDto(merchant);
    }

    @Override
    public void destroyMerchant(String uuid) {
        Merchant merchant = repository.findByReferenceUuid(uuid).orElseThrow(() -> new NotFoundException("Merchant with reference UUID not found: " + uuid));
        if(merchant != null) {
            merchant.setActive(false);
            repository.save(merchant);
        }
    }

    @Override
    public MerchantDto findByUuid(String uuid) {
        Merchant merchant = repository.findByReferenceUuid(uuid).orElseThrow(() -> new NotFoundException("Merchant with reference UUID not found: " + uuid));
        return mapper.convertEntityToDto(merchant);
    }
}
