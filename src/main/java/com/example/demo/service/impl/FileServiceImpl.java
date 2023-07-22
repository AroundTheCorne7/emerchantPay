package com.example.demo.service.impl;

import com.example.demo.exception.FileProcessingException;
import com.example.demo.model.user.Admin;
import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.service.FileService;
import com.example.demo.util.FileUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final MerchantRepository repository;

    @Autowired
    public FileServiceImpl(MerchantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createMerchantsFromCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null.");
        }

        try {
            List<Merchant> merchants = FileUtil.csvToMerchants(file.getInputStream());


            repository.saveAll(merchants);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileProcessingException("Error processing CSV file.", e);
        }
    }

    @Override
    public void createAdminsFromCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null.");
        }

        try {
            List<Admin> admins = FileUtil.csvToAdmin(file.getInputStream());

            // save admins
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileProcessingException("Error processing CSV file.", e);
        }
    }
}
