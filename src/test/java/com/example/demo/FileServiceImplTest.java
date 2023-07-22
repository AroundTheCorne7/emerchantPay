package com.example.demo;

import com.example.demo.exception.FileProcessingException;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.service.impl.FileServiceImpl;
import com.example.demo.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    @Test
    public void testCreateMerchantsFromCsv() throws IOException {
        String csvData = "uuid1,Merchant 1,merchant1@example.com,Description 1\n" +
                "uuid2,Merchant 2,merchant2@example.com,Description 2";
        MockMultipartFile file = new MockMultipartFile("file.csv", csvData.getBytes());

        List<Merchant> merchants = new ArrayList<>();
        merchants.add(new Merchant(1L, "uuid1", "Merchant 1", "merchant1@example.com", "Description 1", true, BigDecimal.valueOf(0)));
        merchants.add(new Merchant(2L, "uuid2", "Merchant 2", "merchant2@example.com", "Description 2", true, BigDecimal.valueOf(0)));
        when(FileUtil.csvToMerchants(any(ByteArrayInputStream.class))).thenReturn(merchants);

        // Call the service method
        fileService.createMerchantsFromCsv(file);

        // Verify repository.saveAll is called with correct arguments
        verify(repository, times(1)).saveAll(merchants);
    }

    @Test
    public void testCreateMerchantsFromCsvEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile("empty.csv", new byte[0]);

        // Call the service method and expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> fileService.createMerchantsFromCsv(emptyFile));

        // Verify that repository.saveAll is not called
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    public void testCreateMerchantsFromCsvProcessingException() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file.csv", "invalid data".getBytes());

        when(FileUtil.csvToMerchants(any(ByteArrayInputStream.class))).thenThrow(IOException.class);

        // Call the service method and expect a FileProcessingException
        assertThrows(FileProcessingException.class, () -> fileService.createMerchantsFromCsv(file));

        // Verify that repository.saveAll is not called
        verify(repository, never()).saveAll(anyList());
    }
}

