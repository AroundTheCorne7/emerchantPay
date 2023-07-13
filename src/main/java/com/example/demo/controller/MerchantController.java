package com.example.demo.controller;

import com.example.demo.model.user.dto.MerchantDto;
import com.example.demo.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService service;

    @GetMapping("/get-all")
    public ResponseEntity<List<MerchantDto>> findAll() {
        return new ResponseEntity(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MerchantDto> findByUuid(@RequestParam String uuid) {
        return new ResponseEntity(service.findByUuid(uuid), HttpStatus.OK);
    }


    @PostMapping("/update")
    public ResponseEntity<MerchantDto> updateMerchant(@RequestBody MerchantDto dto) {
        return new ResponseEntity(service.updateMerchant(dto), HttpStatus.OK);
    }


    @PostMapping("/destroy")
    public ResponseEntity<Void> destroyMerchant(@RequestParam String uuid) {
        service.destroyMerchant(uuid);
        return ResponseEntity.noContent().build();
    }

}
