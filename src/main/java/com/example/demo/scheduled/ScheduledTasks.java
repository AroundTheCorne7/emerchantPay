package com.example.demo.scheduled;

import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.logging.Logger;

@Component
public class ScheduledTasks {

    static final Logger LOGGER =
            Logger.getLogger(ScheduledTasks.class.getName());
    @Autowired
    private TransactionRepository repository;
    @Scheduled(fixedDelay = 3600000)
    public void deleteOldTransactions() {
        LocalTime expiryDate = LocalTime.now().minusHours(1);
        repository.deleteByDateCreatedBefore(expiryDate);
        LOGGER.info("Cron job ran successfully");
    }
}
