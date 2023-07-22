package com.example.demo.controller;

import com.example.demo.service.FileService;
import com.example.demo.service.MerchantService;
import com.example.demo.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService service) {
        this.fileService = service;
    }

    @PostMapping("/merchant")
    private ResponseEntity<Void> importMerchantFromCsv(@RequestParam("file") MultipartFile file) {
        if (FileUtil.hasCSVFormat(file)) {
            fileService.createMerchantsFromCsv(file);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
