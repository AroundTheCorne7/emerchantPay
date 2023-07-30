package com.example.demo.controller;

import com.example.demo.model.user.dto.MerchantDto;
import com.example.demo.service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MerchantControllerTest {

    private MerchantService merchantService;

    private MerchantController merchantController;

    @BeforeEach
    public void setUp() {
        merchantService = mock(MerchantService.class);
        merchantController = new MerchantController(merchantService);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<MerchantDto> mockMerchantList = Collections.singletonList(new MerchantDto());
        when(merchantService.findAll(anyLong())).thenReturn(mockMerchantList);

        // Act
        ResponseEntity<List<MerchantDto>> responseEntity = merchantController.findAll(anyLong());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockMerchantList, responseEntity.getBody());
    }

    @Test
    public void testFindByUuid() {
        // Arrange
        String uuid = "merchant123";
        MerchantDto mockMerchantDto = new MerchantDto();
        when(merchantService.findByUuid(uuid)).thenReturn(mockMerchantDto);

        // Act
        ResponseEntity<MerchantDto> responseEntity = merchantController.findByUuid(uuid);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockMerchantDto, responseEntity.getBody());
    }

    @Test
    public void testUpdateMerchant() {
        // Arrange
        MerchantDto merchantDto = new MerchantDto();
        MerchantDto mockUpdatedMerchantDto = new MerchantDto();
        when(merchantService.updateMerchant(merchantDto)).thenReturn(mockUpdatedMerchantDto);

        // Act
        ResponseEntity<MerchantDto> responseEntity = merchantController.updateMerchant(merchantDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUpdatedMerchantDto, responseEntity.getBody());
    }

    @Test
    public void testDestroyMerchant() {
        // Arrange
        String uuid = "merchant123";

        // Act
        ResponseEntity<Void> responseEntity = merchantController.destroyMerchant(uuid);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(merchantService, times(1)).destroyMerchant(uuid);
    }
}
