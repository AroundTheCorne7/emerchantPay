package com.example.demo.service.impl;

import com.example.demo.exception.FileProcessingException;
import com.example.demo.model.user.Merchant;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRole;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FileService;
import com.example.demo.util.FileUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final MerchantRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public FileServiceImpl(MerchantRepository repository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void createMerchantsFromCsv(MultipartFile file) {
        if (file == null || file.isEmpty() || !FileUtil.hasCSVFormat(file)) {
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
            List<User> admins = csvToAdmin(file.getInputStream());

            userRepository.saveAll(admins);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileProcessingException("Error processing CSV file.", e);
        }
    }

    private List<User> csvToAdmin(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<User> admins = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            UserRole userRole = new UserRole();
            userRole.setRole("Admin");

            for (CSVRecord csvRecord : csvRecords) {
                User merchant = User.builder().username(csvRecord.get("email"))
                        .password(passwordEncoder.encode(csvRecord.get("password"))).roles(List.of(userRole)).build();

                admins.add(merchant);
            }

            return admins;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
