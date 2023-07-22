package com.example.demo;

import com.example.demo.model.user.Merchant;
import com.example.demo.model.user.dto.MerchantDto;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.service.impl.MerchantServiceImpl;
import com.example.demo.util.MerchantMapper;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MerchantServiceImplTest {
    @Mock
    private MerchantRepository repository;

    @Mock
    private MerchantMapper mapper;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @Test
    public void testFindAll() {
        // Prepare test data
        List<Merchant> merchants = new ArrayList<>();
        merchants.add(new Merchant(1L, "uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0)));
        merchants.add(new Merchant(2L, "uuid2", "Merchant 2", "merchant2@example.com", "Description 2", true, BigDecimal.valueOf(0)));

        // Mock repository method
        when(repository.findAllByIsActiveTrue()).thenReturn(merchants);

        // Prepare mapper response
        List<MerchantDto> merchantDtos = new ArrayList<>();
        merchantDtos.add(new MerchantDto("uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0)));
        merchantDtos.add(new MerchantDto("uuid2", "Merchant 2", "merchant2@example.com", "Description 2", true, BigDecimal.valueOf(0)));
        when(mapper.convertEntitiesToDtos(merchants)).thenReturn(merchantDtos);

        // Call the service method
        List<MerchantDto> result = merchantService.findAll();

        // Assertions
        assertEquals(merchantDtos.size(), result.size());
        assertEquals(merchantDtos.get(0).getName(), result.get(0).getName());
        assertEquals(merchantDtos.get(1).getName(), result.get(1).getName());
        // Add more assertions as needed.
    }

    @Test
    public void testUpdateMerchant() {
        // Prepare test data
        MerchantDto merchantDto = new MerchantDto("uuid1", "Updated Merchant", "updated@example.com", "Updated Description", true, BigDecimal.valueOf(0));
        Merchant merchant = new Merchant(1L, "uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));

        // Mock repository method
        when(repository.findByReferenceUuid(merchantDto.getReferenceUuid())).thenReturn(Optional.of(merchant));
        when(repository.save(merchant)).thenReturn(merchant);

        // Call the service method
        MerchantDto result = merchantService.updateMerchant(merchantDto);

        // Assertions
        assertEquals(merchantDto.getName(), result.getName());
        assertEquals(merchantDto.getEmail(), result.getEmail());
        assertEquals(merchantDto.getDescription(), result.getDescription());
        // Add more assertions as needed.
    }

    @Test
    public void testDestroyMerchant() {
        // Prepare test data
        String uuid = "uuid1";
        Merchant merchant = new Merchant(1L, uuid, "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));

        // Mock repository method
        when(repository.findByReferenceUuid(uuid)).thenReturn(Optional.of(merchant));
        when(repository.save(merchant)).thenReturn(merchant);

        // Call the service method
        merchantService.destroyMerchant(uuid);

        // Assertions
        assertFalse(merchant.isActive());
        // Add more assertions as needed.
    }

    @Test
    public void testFindByUuid() {
        // Prepare test data
        String uuid = "uuid1";
        Merchant merchant = new Merchant(1L, uuid, "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));

        // Mock repository method
        when(repository.findByReferenceUuid(uuid)).thenReturn(Optional.of(merchant));

        // Prepare mapper response
        MerchantDto merchantDto = new MerchantDto(uuid, "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0));
        when(mapper.convertEntityToDto(merchant)).thenReturn(merchantDto);

        // Call the service method
        MerchantDto result = merchantService.findByUuid(uuid);

        // Assertions
        assertEquals(merchantDto.getName(), result.getName());
        assertEquals(merchantDto.getEmail(), result.getEmail());
        assertEquals(merchantDto.getDescription(), result.getDescription());
        // Add more assertions as needed.
    }
}
