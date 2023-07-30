package com.example.demo.service;

import com.example.demo.exception.FileProcessingException;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.service.impl.FileServiceImpl;
import com.example.demo.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FileServiceImplTest {

    @Mock
    private MerchantRepository repository;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    public void setUp() {
        repository = mock(MerchantRepository.class);
        fileService = new FileServiceImpl(repository);
    }

    @Test
    public void testCreateMerchantsFromCsv() {
        String csvData = "name, description, email\n" +
                "Merchant 1,merchant1@example.com,Description 1\n" +
                "Merchant 2,merchant2@example.com,Description 2";
        MockMultipartFile file = new MockMultipartFile("file.csv",null, "text/csv", csvData.getBytes());

        fileService.createMerchantsFromCsv(file);

        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCreateMerchantsFromCsvEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile("empty.csv", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> fileService.createMerchantsFromCsv(emptyFile));

        verify(repository, never()).saveAll(anyList());
    }

    @Test
    public void testCreateMerchantsFromCsvProcessingException() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file.csv", null, "text/csv", "invalid data".getBytes());

        assertThrows(FileProcessingException.class, () -> fileService.createMerchantsFromCsv(file));

        verify(repository, never()).saveAll(anyList());
    }

    @Test
    public void testCreateMerchantsFromCsvInvalidContentType() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file.csv", null, null, "invalid data".getBytes());

        assertThrows(IllegalArgumentException.class, () -> fileService.createMerchantsFromCsv(file));

        verify(repository, never()).saveAll(anyList());
    }
}

