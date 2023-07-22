package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void createAdminsFromCsv(MultipartFile file);

    void createMerchantsFromCsv(MultipartFile file);
}
