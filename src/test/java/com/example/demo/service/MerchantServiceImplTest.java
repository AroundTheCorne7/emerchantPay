package com.example.demo.service;

import com.example.demo.controller.MerchantController;
import com.example.demo.model.user.Merchant;
import com.example.demo.model.user.User;
import com.example.demo.model.user.dto.MerchantDto;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MerchantService;
import com.example.demo.service.impl.MerchantServiceImpl;
import com.example.demo.util.MerchantMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MerchantServiceImplTest {

    private MerchantRepository repository;

    private MerchantMapper mapper;

    private UserRepository userRepository;

    private MerchantServiceImpl merchantService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        mapper = mock(MerchantMapper.class);
        repository = mock(MerchantRepository.class);
        merchantService = new MerchantServiceImpl(repository, mapper, userRepository);
    }


    @Test
    public void testFindAll() {
        List<Merchant> merchants = new ArrayList<>();
        merchants.add(new Merchant(1L, "uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0)));
        merchants.add(new Merchant(2L, "uuid2", "Merchant 2", "merchant2@example.com", "Description 2", true, BigDecimal.valueOf(0)));

        List<MerchantDto> merchantDtos = new ArrayList<>();
        merchantDtos.add(new MerchantDto("uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0)));
        merchantDtos.add(new MerchantDto("uuid2", "Merchant 2", "merchant2@example.com", "Description 2", true, BigDecimal.valueOf(0)));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(mapper.convertEntitiesToDtos(merchants)).thenReturn(merchantDtos);
        when(repository.findByReferenceUuidAndIsActiveTrue(null)).thenReturn(merchants);


        List<MerchantDto> result = merchantService.findAll(anyLong());

        assertEquals(merchantDtos.size(), result.size());
        assertEquals(merchantDtos.get(0).getName(), result.get(0).getName());
        assertEquals(merchantDtos.get(1).getName(), result.get(1).getName());
    }

    @Test
    public void testUpdateMerchant() {
        MerchantDto merchantDto = new MerchantDto("uuid1", "Updated Merchant", "updated@example.com", "Updated Description", true, BigDecimal.valueOf(0));
        Merchant merchant = new Merchant(1L, "uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));

        when(repository.findByReferenceUuid(merchantDto.getReferenceUuid())).thenReturn(Optional.of(merchant));
        when(repository.save(merchant)).thenReturn(merchant);
        when(mapper.convertEntityToDto(merchant)).thenReturn(merchantDto);

        MerchantDto result = merchantService.updateMerchant(merchantDto);

        assertEquals(merchantDto.getName(), result.getName());
        assertEquals(merchantDto.getEmail(), result.getEmail());
        assertEquals(merchantDto.getDescription(), result.getDescription());
    }

    @Test
    public void testDestroyMerchant() {
        String uuid = "uuid1";
        Merchant merchant = new Merchant(1L, uuid, "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));

        when(repository.findByReferenceUuid(uuid)).thenReturn(Optional.of(merchant));
        when(repository.save(merchant)).thenReturn(merchant);

        merchantService.destroyMerchant(uuid);

        assertFalse(merchant.isActive());
    }

    @Test
    public void testFindByUuid() {
        String uuid = "uuid1";
        Merchant merchant = new Merchant(1L, uuid, "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));

        when(repository.findByReferenceUuid(uuid)).thenReturn(Optional.of(merchant));

        MerchantDto merchantDto = new MerchantDto(uuid, "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));
        when(mapper.convertEntityToDto(merchant)).thenReturn(merchantDto);

        MerchantDto result = merchantService.findByUuid(uuid);

        assertEquals(merchantDto.getName(), result.getName());
        assertEquals(merchantDto.getEmail(), result.getEmail());
        assertEquals(merchantDto.getDescription(), result.getDescription());
    }
}
