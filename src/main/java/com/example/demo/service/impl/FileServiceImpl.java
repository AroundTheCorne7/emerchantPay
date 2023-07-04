package com.example.demo.service.impl;

import com.example.demo.model.user.Merchant;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.service.FileService;
import com.example.demo.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class FileServiceImpl implements FileService {

    @Autowired
    private MerchantRepository repository;

    @Override
    public void createMerchantFromFile(MultipartFile file) {
        try {
            List<Merchant> merchants = FileUtil.csvToMerchants(file.getInputStream());
            repository.saveAll(merchants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAdminFromFile(MultipartFile file) {
        try {
            List<Merchant> merchants = FileUtil.csvToMerchants(file.getInputStream());
            repository.saveAll(merchants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
