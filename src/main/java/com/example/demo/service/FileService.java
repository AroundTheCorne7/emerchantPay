package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void createMerchantFromFile(MultipartFile file);

    void createAdminFromFile(MultipartFile file);
}
