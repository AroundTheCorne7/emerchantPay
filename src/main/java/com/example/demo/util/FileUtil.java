package com.example.demo.util;

import com.example.demo.exception.FileProcessingException;
import com.example.demo.model.user.Merchant;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRole;
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
import java.util.UUID;

@Service
public class FileUtil {

    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static List<Merchant> csvToMerchants(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Merchant> merchants = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            if(!csvRecords.iterator().hasNext()) {
                throw new FileProcessingException("File is not a correct csv");
            }

            for (CSVRecord csvRecord : csvRecords) {
                String referenceId = UUID.randomUUID().toString();
                Merchant merchant = Merchant.builder().email(csvRecord.get("email"))
                        .name(csvRecord.get("name")).description(csvRecord.get("description")).isActive(true).referenceUuid(referenceId).build();

                merchants.add(merchant);
            }

            return merchants;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }


}
